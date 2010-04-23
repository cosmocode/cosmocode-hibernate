/**
 * Copyright 2010 CosmoCode GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.cosmocode.hibernate;

import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

/**
 * A custom version of {@link Restrictions}.
 * 
 * @author Willi Schoenborn
 */
public final class CustomRestrictions {

    /**
     * Prevent instantiation.
     */
    private CustomRestrictions() {
        
    }

    /**
     * Group expressions together in a single disjunction (A or B or C...).
     * 
     * @param first the first {@link Criterion}
     * @param second the second {@link Criterion}
     * @param rest the rest
     * @return a {@link Criterion} containing all parameters combined in disjunct style
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
     * Group expressions together in a single conjunction (A and B and C...).
     * 
     * @param first the first {@link Criterion}
     * @param second the second {@link Criterion}
     * @param rest the rest
     * @return a {@link Criterion} containing all parameters combined in conjuct style
     */
    public static Criterion conjunction(Criterion first, Criterion second, Criterion... rest) {
        final Conjunction conjunction = Restrictions.conjunction();
        conjunction.add(first).add(second);
        for (Criterion criterion : rest) {
            conjunction.add(criterion);
        }
        return conjunction;
    }
    
    /**
     * Apply an "equal" constraint to the named property.
     * 
     * <p>
     *   Note: This implementation differs from {@link Restrictions#eq(String, Object)}
     *   because it checks for empty strings and applies {@link CustomRestrictions#isEmpty(String)}
     *   instead.
     * </p>
     * 
     * @param propertyName the name of the property the constraint should be applied to
     * @param value the actual value the property should be equals to
     * @return a new {@link Criterion}
     */
    public static Criterion eq(String propertyName, String value) {
        return StringUtils.isEmpty(value) ?
            CustomRestrictions.isEmpty(propertyName) :
            Restrictions.eq(propertyName, value);
    }
    
    /**
     * Apply a "not equal" constraint to the named property.
     * 
     * <p>
     *   Note: This implementation differs from {@link Restrictions#ne(String, Object)}
     *   because it returns {@link CustomRestrictions#isNotEmpty(String)}
     *   in case value is an empty string and returns an logical or expression
     *   of {@link Restrictions#ne(String, Object)} and {@link Restrictions#isNull(String)}.
     * </p>
     * 
     * @param propertyName the name of the property the constraint should be applied to
     * @param value the actual value the property should be not equals to
     * @return a new {@link Criterion}
     */
    public static Criterion ne(String propertyName, String value) {
        if (StringUtils.isEmpty(value)) {
            return CustomRestrictions.isNotEmpty(propertyName);
        } else {
            return Restrictions.or(
                Restrictions.ne(propertyName, value),
                Restrictions.isNull(propertyName)
            );
        }
    }
    
    /**
     * Apply an "empty" constraint on the named property.
     * 
     * <p>
     *   See also {@link StringUtils#isEmpty(String)}
     * </p>
     * 
     * @param propertyName the name of the property the constraint should be applied to
     * @return a new {@link Criterion}
     */
    public static Criterion isEmpty(String propertyName) {
        return Restrictions.or(
            Restrictions.eq(propertyName, ""),
            Restrictions.isNull(propertyName)
        );
    }
    
    /**
     * Apply a "not empty" constraint to the named property.
     * 
     * <p>
     *   See also {@link StringUtils#isNotEmpty(String)}
     * </p>
     * 
     * @param propertyName the name of the property the constraint should be applied to
     * @return a new {@link Criterion}
     */
    public static Criterion isNotEmpty(String propertyName) {
        return Restrictions.not(CustomRestrictions.isEmpty(propertyName));
    }
    
    /**
     * Apply an "ilike" constraint on the named property.
     * 
     * <p>
     *   Note: This implementation differs from {@link Restrictions#ilike(String, String, MatchMode)}
     *   because it checks for empty strings and applies {@link CustomRestrictions#isEmpty(String)}
     *   instead.
     * </p>
     * 
     * @param propertyName the name of the property the constraint should be applied to
     * @param value the actual value the property should be similiar to
     * @param matchMode the {@link MatchMode} being used
     * @return a new {@link Criterion}
     */
    public static Criterion ilike(String propertyName, String value, MatchMode matchMode) {
        return StringUtils.isEmpty(value) ? 
            CustomRestrictions.isEmpty(propertyName) : 
            Restrictions.ilike(propertyName, value, matchMode);
    }
    
    /**
     * Apply an "ilike" constraint on the named property.
     * 
     * <p>
     *   Note: This implementation differs from {@link Restrictions#ilike(String, Object)}
     *   because it checks for empty strings and applies {@link CustomRestrictions#isEmpty(String)}
     *   instead.
     * </p>
     * 
     * <p>
     *   Its equivalent to calling {@link CustomRestrictions#ilike(String, String, MatchMode)}
     *   using {@link MatchMode#ANYWHERE}.
     * </p>
     * 
     * @param propertyName the name of the property the constraint should be applied to
     * @param value the actual value the property should be similiar to
     * @return a new {@link Criterion}
     */
    public static Criterion ilike(String propertyName, String value) {
        return ilike(propertyName, value, MatchMode.ANYWHERE);
    }
    
    /**
     * Apply a "not ilike" constraint on the named property.
     * 
     * <p>
     *   This implementation handles empty values correctly.
     * </p>
     * 
     * @param propertyName the name of the property the constraint should be applied to
     * @param value the actual value the property should be similiar to
     * @param matchMode the {@link MatchMode} being used
     * @return a new {@link Criterion}
     */
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
    
    /**
     * Apply a "not ilike" constraint on the named property.
     * 
     * <p>
     *   This implementation handles empty values correctly.
     * </p>
     * 
     * <p>
     *   Its equivalent to calling {@link CustomRestrictions#notIlike(String, String, MatchMode)}
     *   using {@link MatchMode#ANYWHERE}.
     * </p>
     * 
     * @param propertyName the name of the property the constraint should be applied to
     * @param value the actual value the property should be similiar to
     * @return a new {@link Criterion}
     */
    public static Criterion notIlike(String propertyName, String value) {
        return notIlike(propertyName, value, MatchMode.ANYWHERE);
    }
    
    /**
     * Apply a "reverse ilike" expression on the named property.
     * 
     * @see ReverseIlikeExpression
     * @see PropertyMatchMode
     * 
     * @param propertyName the name of the property the constraint should be applied to
     * @param value the actual value which should be similiar to the named property
     * @param matchMode the {@link PropertyMatchMode} being used
     * @return a new {@link Criterion}
     */
    public static Criterion reverseIlike(String propertyName, String value, PropertyMatchMode matchMode) {
        return new ReverseIlikeExpression(propertyName, value, matchMode);
    }

    /**
     * Apply a "reverse ilike" expression on the named property.
     * 
     * <p>
     *   Its equivalent to calling {@link CustomRestrictions#reverseIlike(String, String, PropertyMatchMode)}
     *   using {@link PropertyMatchMode#ANYWHERE}
     * </p>
     * 
     * @see ReverseIlikeExpression
     * 
     * @param propertyName the name of the property the constraint should be applied to
     * @param value the actual value which should be similiar to the named property
     * @return a new {@link Criterion}
     */
    public static Criterion reverseIlike(String propertyName, String value) {
        return CustomRestrictions.reverseIlike(propertyName, value, PropertyMatchMode.ANYWHERE);
    }

    /**
     * Apply a "not reverse ilike" expression on the named property.
     * 
     * @see ReverseIlikeExpression
     * @see PropertyMatchMode
     * 
     * @param propertyName the name of the property the constraint should be applied to
     * @param value the actual value which should not be similiar to the named property
     * @param matchMode the {@link PropertyMatchMode} being used
     * @return a new {@link Criterion}
     */
    public static Criterion notReverseIlike(String propertyName, String value, PropertyMatchMode matchMode) {
        return Restrictions.not(CustomRestrictions.reverseIlike(propertyName, value, matchMode));
    }

    /**
     * Apply a "not reverse ilike" expression on the named property.
     * 
     * <p>
     *   Its equivalent to calling {@link CustomRestrictions#notReverseIlike(String, String, PropertyMatchMode)}
     *   using {@link PropertyMatchMode#ANYWHERE}
     * </p>
     * 
     * @see ReverseIlikeExpression
     * 
     * @param propertyName the name of the property the constraint should be applied to
     * @param value the actual value which should not be similiar to the named property
     * @return a new {@link Criterion}
     */
    public static Criterion notReverseIlike(String propertyName, String value) {
        return CustomRestrictions.notReverseIlike(propertyName, value, PropertyMatchMode.ANYWHERE);
    }
    
    /**
     * Apply a "has" constraint to the named enumset property.
     * 
     * @param <E> the generic enum type
     * @param propertyName the name of the property the constraint should be applied to
     * @param e the enum value the named property should contain
     * @return a new {@link Criterion}
     */
    public static <E extends Enum<E>> Criterion has(String propertyName, E e) {
        return new EnumSetRestriction<E>(propertyName, "&", e, ">", 0);
    }

    /**
     * Apply a "all" constraint to the named enumset property.
     * 
     * @param <E> the generic enum type
     * @param propertyName the name of the property the constraint should be applied to
     * @param enums the set of enums the named property should contain
     * @return a new {@link Criterion}
     */
    public static <E extends Enum<E>> Criterion all(String propertyName, Set<E> enums) {
        return new EnumSetRestriction<E>(propertyName, "&", enums, ">", 0);
    }

    /**
     * Apply a "not has" constraint to the named enumset property.
     * 
     * @param <E> the generic enum type
     * @param propertyName the name of the property the constraint should be applied to
     * @param e the enum value the named property should not contain
     * @return a new {@link Criterion}
     */
    public static <E extends Enum<E>> Criterion notHas(String propertyName, E e) {
        return new EnumSetRestriction<E>(propertyName, "&", e, "=", 0); 
    }

    /**
     * Apply a "none" constraint to the named enumset property.
     * 
     * @param <E> the generic enum type
     * @param propertyName the name of the property the constraint should be applied to
     * @param enums the set of enums the named property should not contain
     * @return a new {@link Criterion}
     */
    public static <E extends Enum<E>> Criterion none(String propertyName, Set<E> enums) {
        return new EnumSetRestriction<E>(propertyName, "&", enums, "=", 0);
    }
    
}
