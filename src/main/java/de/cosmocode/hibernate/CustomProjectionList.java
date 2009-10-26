package de.cosmocode.hibernate;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;

public class CustomProjectionList extends ProjectionList {
	
	private static final long serialVersionUID = -7323390575260218801L;
	
	public String toSqlString(Criteria criteria, int loc, CriteriaQuery criteriaQuery) 
	throws HibernateException {
		StringBuffer buf = new StringBuffer();
		for ( int i=0; i<getLength(); i++ ) {
			Projection proj = getProjection(i);
			buf.append( proj.toSqlString(criteria, loc, criteriaQuery) );
			loc += proj.getColumnAliases(loc).length;
			if ( i<getLength()-1 && !getProjection(i+1).isGrouped()) buf.append(", ");
		}
		return buf.toString();
	}
	
}