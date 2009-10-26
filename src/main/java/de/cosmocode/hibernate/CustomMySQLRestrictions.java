package de.cosmocode.hibernate;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

public final class CustomMySQLRestrictions {

	private CustomMySQLRestrictions() {
		
	}
	
	private static Criterion bit(String columnName, String binaryOperator, long flag, String operator, long value) {
	    return Restrictions.sqlRestriction("({alias}." + columnName + " " + binaryOperator + " " + flag + ") " + operator + " " + value);
	}
	
	public static Criterion bitAndGt(String columnName, long flag, long value) {
	    return CustomMySQLRestrictions.bit(columnName, "&", flag, ">", value);
	}
	
	public static Criterion bitContains(String columnName, long flag) {
	    return CustomMySQLRestrictions.bitAndGt(columnName, flag, 0);
	}
	
}