package de.cosmocode.hibernate;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.PropertyProjection;

/**
 * A {@link Projection} which does not
 * add the column being used for grouping
 * to the select clause (as {@link PropertyProjection} does).
 *
 * @author Willi Schoenborn
 */
public class GroupOnlyProjection extends PropertyProjection {

    private static final long serialVersionUID = 5181025087712804297L;

    public GroupOnlyProjection(String prop) {
        super(prop, true);
    }

    @Override
    public String toSqlString(Criteria criteria, int position, CriteriaQuery criteriaQuery) throws HibernateException {
        return isGrouped() ? "" : super.toSqlString(criteria, position, criteriaQuery);
    }

}
