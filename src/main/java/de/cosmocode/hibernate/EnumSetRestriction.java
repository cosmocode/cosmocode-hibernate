package de.cosmocode.hibernate;

import java.util.EnumSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.engine.TypedValue;

import de.cosmocode.commons.Enums;

/**
 * A {@link Criterion} used to restrict by {@link EnumSet}s
 * being stored as bitsets.
 * 
 * @see EnumSetUserType
 *
 * @author Willi Schoenborn
 * @param <E> the generic enum type
 */
public class EnumSetRestriction<E extends Enum<E>> implements Criterion {

    private static final long serialVersionUID = 2754183680699907609L;
    
    private static final TypedValue[] NO_VALUES = {};
    
    private final String propertyName;
    private final Set<E> enums;
    private final String bitOp;
    private final String op;
    private final long value;
    
    public EnumSetRestriction(String propertyName, String bitOp, E e, String op, long value) {
        this(propertyName, bitOp, EnumSet.of(e), op, value);
    }
    
    public EnumSetRestriction(String propertyName, String bitOp, Set<E> enums, String op, long value) {
        this.propertyName = propertyName;
        this.enums = enums;
        this.bitOp = bitOp;
        this.op = op;
        this.value = value;
    }

    @Override
    public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
        final String[] columns = criteriaQuery.getColumnsUsingProjection(criteria, propertyName);
        final String[] fragments = new String[columns.length];
        
        for (int i = 0; i < columns.length; i++) {
            fragments[i] = 
                "(" + columns[i] + " " + bitOp + " " + Enums.encode(enums) + " " + op + " " + value + ")";
        }
        
        return StringUtils.join(fragments, " and ");
    }
    
    @Override
    public TypedValue[] getTypedValues(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
        return NO_VALUES; 
    }
    
}
