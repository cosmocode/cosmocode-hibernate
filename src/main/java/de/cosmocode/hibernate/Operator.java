package de.cosmocode.hibernate;

import java.util.Calendar;
import java.util.Date;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import de.cosmocode.commons.Calendars;

/**
 * Used to avoid string comparisons.
 *
 * @author Willi Schoenborn
 */
public enum Operator {
    
    GT {
        
        @Override
        public Criterion restrict(String propertyName, Object value) {
            return Restrictions.gt(propertyName, value);
        }
        
        @Override
        public Criterion restrictDate(String propertyName, Calendar calendar) {
            Calendars.toEndOfTheDay(calendar);
            return Restrictions.gt(propertyName, calendar.getTime());
        }
        
        @Override
        public <E extends Enum<E>> Criterion restrictEnum(String propertyName, E e) {
            throw new UnsupportedOperationException();
        };
        
        @Override
        public Criterion restrictCollection(String propertyName, int size) {
            return Restrictions.sizeGt(propertyName, size);
        }
        
    },
    
    GE {
        
        @Override
        public Criterion restrict(String propertyName, Object value) {
            return Restrictions.ge(propertyName, value);
        }
        
        @Override
        public Criterion restrictDate(String propertyName, Calendar calendar) {
            Calendars.toBeginningOfTheDay(calendar);
            return Restrictions.ge(propertyName, calendar.getTime());
        }
        
        @Override
        public <E extends Enum<E>> Criterion restrictEnum(String propertyName, E e) {
            throw new UnsupportedOperationException();
        };
        
        @Override
        public Criterion restrictCollection(String propertyName, int size) {
            return Restrictions.sizeGe(propertyName, size);
        }
        
    }, 
    
    EQ {
        
        @Override
        public Criterion restrict(String propertyName, Object value) {
            return Restrictions.gt(propertyName, value);
        }
        
        @Override
        public Criterion restrictDate(String propertyName, Calendar calendar) {
            Calendars.toBeginningOfTheDay(calendar);
            final Date begin = calendar.getTime();
            Calendars.toEndOfTheDay(calendar);
            final Date end = calendar.getTime();
            return Restrictions.between(propertyName, begin, end);
        }
        
        @Override
        public <E extends Enum<E>> Criterion restrictEnum(String propertyName, E e) {
            return restrict(propertyName, e);
        };
        
        @Override
        public Criterion restrictCollection(String propertyName, int size) {
            return Restrictions.sizeEq(propertyName, size);
        }
        
    }, 
    
    LE {
        
        @Override
        public Criterion restrict(String propertyName, Object value) {
            return Restrictions.le(propertyName, value);
        }
        
        @Override
        public Criterion restrictDate(String propertyName, Calendar calendar) {
            Calendars.toEndOfTheDay(calendar);
            return Restrictions.le(propertyName, calendar.getTime());
        }
        
        @Override
        public <E extends Enum<E>> Criterion restrictEnum(String propertyName, E e) {
            throw new UnsupportedOperationException();
        };
        
        @Override
        public Criterion restrictCollection(String propertyName, int size) {
            return Restrictions.sizeLe(propertyName, size);
        }
        
    }, 
    
    LT {
        
        @Override
        public Criterion restrict(String propertyName, Object value) {
            return Restrictions.lt(propertyName, value);
        }
        
        @Override
        public Criterion restrictDate(String propertyName, Calendar calendar) {
            Calendars.toBeginningOfTheDay(calendar);
            return Restrictions.lt(propertyName, calendar.getTime());
        }
        
        @Override
        public <E extends Enum<E>> Criterion restrictEnum(String propertyName, E e) {
            throw new UnsupportedOperationException();
        };
        
        @Override
        public Criterion restrictCollection(String propertyName, int size) {
            return Restrictions.sizeLt(propertyName, size);
        }
        
    }, 
    
    NE {
        
        @Override
        public Criterion restrict(String propertyName, Object value) {
            return Restrictions.ne(propertyName, value);
        }
        
        @Override
        public Criterion restrictDate(String propertyName, Calendar calendar) {
            return Restrictions.not(
                Operator.EQ.restrictDate(propertyName, calendar)
            );
        }
        
        @Override
        public <E extends Enum<E>> Criterion restrictEnum(String propertyName, E e) {
            return Restrictions.or(
                Restrictions.ne(propertyName, e),
                Restrictions.isNull(propertyName)
            );
        };
        
        @Override
        public Criterion restrictCollection(String propertyName, int size) {
            return Restrictions.sizeNe(propertyName, size);
        }
        
    };
    
    /**
     * Apply the implicit constraint of this {@link Operator} to
     * the named property.
     * 
     * @param propertyName the name of the property the constraint should be applied to
     * @param value the actual value
     * @return a new {@link Criterion}
     */
    public abstract Criterion restrict(String propertyName, Object value);
    
    /**
     * Apply the implicit constraint of this {@link Operator} to
     * the named property.
     * 
     * @param propertyName the name of the property the constraint should be applied to
     * @param value the actual value
     * @return a new {@link Criterion}
     */
    public abstract Criterion restrictDate(String propertyName, Calendar value);

    /**
     * Apply the implicit constraint of this {@link Operator} to
     * the named property.
     * 
     * @param propertyName the name of the property the constraint should be applied to
     * @param value the actual value
     * @return a new {@link Criterion}
     */
    public Criterion restrictDate(String propertyName, Date value) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(value);
        return restrictDate(propertyName, calendar);
    }
    
    /**
     * Apply the implicit constraint of this {@link Operator} to
     * the named property.
     * 
     * <p>
     *   <strong>Note:</strong> This is an optional operation,
     *   not every {@link Operator} is implementing it.
     * </p>
     * 
     * @param <E> the generic enum type
     * @param propertyName the name of the property the constraint should be applied to
     * @param value the actual value
     * @return a new {@link Criterion}
     */
    public abstract <E extends Enum<E>> Criterion restrictEnum(String propertyName, E value);
    
    /**
     * Apply the implicit constraint of this {@link Operator}
     * the named collection property.
     * 
     * @param propertyName the name of the collection property the constraint should be applied to
     * @param size the size the constraint should be work with
     * @return a new {@link Criterion}
     */
    public abstract Criterion restrictCollection(String propertyName, int size);
    
}
