package de.cosmocode.hibernate;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.PostgreSQLDialect;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.engine.TypedValue;

public class ReverseIlikeExpression implements Criterion {

    private static final long serialVersionUID = -7965682008508199091L;
    
    private final String propertyName;
    private final Object value;
    private final PropertyMatchMode matchMode;

    protected ReverseIlikeExpression(String propertyName, String value) {
        this(propertyName, value, PropertyMatchMode.EXACT);
    }

    protected ReverseIlikeExpression(String propertyName, String value, PropertyMatchMode matchMode) {
        this.propertyName = propertyName;
        this.value = value;
        this.matchMode = matchMode;
    }

    @Override
    public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
        final SessionFactoryImplementor factory = criteriaQuery.getFactory();
        final Dialect dialect = factory.getDialect();
        final String[] columns = criteriaQuery.getColumnsUsingProjection(criteria, propertyName);
        
        if (columns.length != 1) throw new HibernateException("ilike may only be used with single-column properties");
        
        final String columnName = columns[0];
        final String s = matchMode.toMatchString(factory, columnName);
        
        if (dialect instanceof PostgreSQLDialect) {
            return "? ilike " + s;
        } else {
            return "? like " + dialect.getLowercaseFunction() + "(" + s + ")";
        }

        
    }

    @Override
    public TypedValue[] getTypedValues(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
        return new TypedValue[] { criteriaQuery.getTypedValue(criteria, propertyName, value.toString().toLowerCase()) };
    }

    public String toString() {
        return value + " ilike " + propertyName;
    }
    
}
