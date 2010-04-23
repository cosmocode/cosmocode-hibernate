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
