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
