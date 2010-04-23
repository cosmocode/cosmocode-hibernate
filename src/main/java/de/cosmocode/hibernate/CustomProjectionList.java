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
import org.hibernate.criterion.ProjectionList;

/**
 * A custom {@link ProjectionList} which handles
 * {@link GroupOnlyProjection}s correctly.
 *
 * @author Willi Schoenborn
 */
public class CustomProjectionList extends ProjectionList {
    
    private static final long serialVersionUID = -7323390575260218801L;
    
    @Override
    public String toSqlString(Criteria criteria, int loc, CriteriaQuery criteriaQuery) throws HibernateException {
        final StringBuilder buf = new StringBuilder();
        for (int i = 0; i < getLength(); i++) {
            final Projection proj = getProjection(i);
            buf.append(proj.toSqlString(criteria, loc, criteriaQuery));
            proj.getColumnAliases(loc);
            if (i < getLength() - 1 && !getProjection(i + 1).isGrouped()) buf.append(", ");
        }
        return buf.toString();
    }
    
}
