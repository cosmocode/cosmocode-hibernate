package de.cosmocode.hibernate;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

public final class CustomRestrictions {

	private CustomRestrictions() {
		
	}

    /**
     * Group expressions together in a single disjunction (A or B or C...)
     *
     * @return Conjunction
     */
	public static Criterion disjunction(Criterion first, Criterion second, Criterion... rest) {
		final Disjunction disjunction = Restrictions.disjunction();
		disjunction.add(first).add(second);
		for (Criterion criterion : rest) {
			disjunction.add(criterion);
		}
		return disjunction;
	}

    /**
     * Group expressions together in a single conjunction (A and B and C...)
     *
     * @return Conjunction
     */
	public static Criterion conjunction(Criterion first, Criterion second, Criterion... rest) {
		final Conjunction conjunction = Restrictions.conjunction();
		conjunction.add(first).add(second);
		for (Criterion criterion : rest) {
			conjunction.add(criterion);
		}
		return conjunction;
	}
	
	public static Criterion eq(String propertyName, String value) {
		return StringUtils.isEmpty(value) ?
			CustomRestrictions.isEmpty(propertyName) :
			Restrictions.eq(propertyName, value);
	}
	
	public static Criterion ne(String propertyName, String value) {
		return StringUtils.isEmpty(value) ?
			CustomRestrictions.isNotEmpty(propertyName) :
			CustomRestrictions.neOrNull(propertyName, value);
	}
	
	public static Criterion neOrNull(String propertyName, Object value) {
		return Restrictions.or(
			Restrictions.ne(propertyName, value), 
			Restrictions.isNull(propertyName)
		);
	}
	
	public static Criterion isEmpty(String propertyName) {
		return Restrictions.or(
			Restrictions.eq(propertyName, ""),
			Restrictions.isNull(propertyName)
		);
	}
	
	public static Criterion isNotEmpty(String propertyName) {
		return Restrictions.and(
			Restrictions.ne(propertyName, ""),
			Restrictions.isNotNull(propertyName)
		);
	}
	
	public static Criterion ilike(String propertyName, String value, MatchMode matchMode) {
		return StringUtils.isEmpty(value) ? 
			CustomRestrictions.isEmpty(propertyName) : 
			Restrictions.ilike(propertyName, value, matchMode);
	}
	
	public static Criterion ilike(String propertyName, String value) {
		return ilike(propertyName, value, MatchMode.ANYWHERE);
	}
	
	public static Criterion notIlike(String propertyName, String value, MatchMode matchMode) {
	    if (StringUtils.isEmpty(value)) {
	        return CustomRestrictions.isNotEmpty(propertyName);
	    } else {
            return Restrictions.or(
                Restrictions.not(Restrictions.ilike(propertyName, value, matchMode)),
                CustomRestrictions.isEmpty(propertyName)
            );
	    }
	}
	
	public static Criterion notIlike(String propertyName, String value) {
		return notIlike(propertyName, value, MatchMode.ANYWHERE);
	}
    
	public static Criterion reverseIlike(String value, String propertyName, PropertyMatchMode matchMode) {
	    return new ReverseIlikeExpression(propertyName, value, matchMode);
	}
	
	public static Criterion reverseIlike(String value, String propertyName) {
	    return new ReverseIlikeExpression(propertyName, value);
	}
	
	public static Criterion notReverseIlike(String value, String propertyName, PropertyMatchMode matchMode) {
	    return Restrictions.not(reverseIlike(value, propertyName, matchMode));
	}
	
	public static Criterion notReverseIlike(String value, String propertyName) {
	    return Restrictions.not(reverseIlike(value, propertyName));
	}
	
	/*
	 * OLD STUFF, below
	 */
	
	public static Criterion eqOrNull(String propertyName, Object value) {
		return Restrictions.or(
			Restrictions.eq(propertyName, value), 
			Restrictions.isNull(propertyName)
		);
	}
	
	public static Criterion eqOrEqOrNull(String propertyName, boolean value) {
		return value ? 
			Restrictions.eq(propertyName, value) : 
			CustomRestrictions.eqOrNull(propertyName, value);
	}
	
	public static Criterion eqOrBlank(String propertyName, String value) {
		return StringUtils.isEmpty(value) ? 
			CustomRestrictions.isEmpty(propertyName) : 
			Restrictions.eq(propertyName, value);
	}
	
	public static Criterion neOrBlank(String propertyName, String value) {
		return StringUtils.isEmpty(value) ? 
			CustomRestrictions.isNotEmpty(propertyName) : 
			Restrictions.ne(propertyName, value);
	}
	
	public static Criterion integer(String propertyName, int value, Operator operator) {
		switch (operator) {
    		case GT: {
    		    return Restrictions.gt(propertyName, value);
    		}
    		case GE: {
    		    return Restrictions.ge(propertyName, value);
    		}
    		case EQ: {
    		    return Restrictions.eq(propertyName, value);
    		}
    		case NE: {
    		    return Restrictions.ne(propertyName, value);
    		}
    		case LE: {
    		    return Restrictions.le(propertyName, value);
    		}
    		case LT: {
    		    return Restrictions.lt(propertyName, value);
    		}
    		default: {
    		    throw new IllegalArgumentException(operator.toString());
    		}
		}
	}
    
    public static Criterion number(String propertyName, Number value, Operator operator) {
        switch (operator) {
            case GT : {
                return Restrictions.gt(propertyName, value);
            }
            case GE : {
                return Restrictions.ge(propertyName, value);
            }
            case EQ : {
                return Restrictions.eq(propertyName, value);
            }
            case NE : {
                return Restrictions.ne(propertyName, value);
            }
            case LE : {
                return Restrictions.le(propertyName, value);
            }
            case LT : {
                return Restrictions.lt(propertyName, value);
            }
            default : {
                throw new IllegalArgumentException(operator.toString());
            }
        }
    }
	
	public static Criterion date(String propertyName, Date date, Operator operator) {
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		switch (operator) {
			case GT : {
			    return Restrictions.gt(propertyName, date);
			}
			case LT : {
			    return Restrictions.lt(propertyName, date);
			}
			case EQ : 
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.HOUR, 0);
				final Date begin = calendar.getTime();
				calendar.add(Calendar.DAY_OF_MONTH, 1);
				final Date end = calendar.getTime();
				return Restrictions.between(propertyName, begin, end);
			case GE :
				calendar.set(Calendar.HOUR, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.HOUR, 0);
				return Restrictions.ge(propertyName, calendar.getTime());
			case LE :
				calendar.set(Calendar.HOUR, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.HOUR, 0);
				calendar.add(Calendar.DAY_OF_MONTH, 1);
				return Restrictions.le(propertyName, calendar.getTime());
			default : {
			    throw new IllegalArgumentException(operator.toString());
			}
		}
	}
	
	public static Criterion enumerated(String propertyName, Enum<?> value, Operator operator) {
		switch (operator) {
			case EQ : {
			    return Restrictions.eq(propertyName, value);
			}
			case NE : {
			    return CustomRestrictions.neOrNull(propertyName, value);
			}
			default : {
			    throw new IllegalArgumentException(operator.toString());
			}
		}
	}

	public static Criterion collection(String propertyName, int size, Operator operator) {
		switch (operator) {
			case GT : {
			    return Restrictions.sizeGt(propertyName, size);
			}
			case LT : {
			    return Restrictions.sizeLt(propertyName, size);
			}
			case EQ : {
			    return Restrictions.sizeEq(propertyName, size);
			}
			case GE : {
			    return Restrictions.sizeGe(propertyName, size);
			}
			case LE : {
			    return Restrictions.sizeLe(propertyName, size);
			}
			default : {
			    throw new IllegalArgumentException(operator.toString());
			}
		}
	}
	
	public static <E extends Enum<E>> Criterion has(String propertyName, E e) {
	    return new EnumSetRestriction<E>(propertyName, "&", e, ">", 0);
	}
	
	public static <E extends Enum<E>> Criterion all(String propertyName, Set<E> enums) {
	    return new EnumSetRestriction<E>(propertyName, "&", enums, ">", 0);
	}
	
	public static <E extends Enum<E>> Criterion ne(String propertyName, E e) {
	    return new EnumSetRestriction<E>(propertyName, "&", e, "=", 0); 
	}
	
	public static <E extends Enum<E>> Criterion none(String propertyName, Set<E> enums) {
	    return new EnumSetRestriction<E>(propertyName, "&", enums, "=", 0);
	}
	
}
