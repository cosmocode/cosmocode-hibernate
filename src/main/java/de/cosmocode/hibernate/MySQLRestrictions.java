package de.cosmocode.hibernate;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

/**
 * A utility class similiar to {@link Restrictions}
 * containing only MySQL specific sql code.
 *
 * @author Willi Schoenborn
 */
public final class MySQLRestrictions {

    private MySQLRestrictions() {
        
    }
    
    private static Criterion bit(String columnName, String binaryOperator, long flag, String operator, long value) {
        return Restrictions.sqlRestriction(
            "({alias}." + columnName + " " + binaryOperator + " " + flag + ") " + operator + " " + value
        );
    }
    
    /**
     * Apply a "contains" constraint to a bitset column.
     * 
     * @param columnName the name of the column the constraint should be applied to
     * @param flag the bits the column should contain
     * @return a new {@link Criterion}
     */
    public static Criterion bitContains(String columnName, long flag) {
        return MySQLRestrictions.bit(columnName, "&", flag, ">", 0);
    }
    
}
