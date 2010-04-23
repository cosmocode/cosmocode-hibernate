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

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

import de.cosmocode.commons.Enums;

/**
 * A {@link UserType} for storing {@link EnumSet}s as bitsets.
 *
 * @author Willi Schoenborn
 * @param <E> the generic enum type
 */
public abstract class EnumSetUserType<E extends Enum<E>> implements UserType {
    
    private static final int[] SQL_TYPES = {Types.BIGINT};
    
    private final Class<E> type;

    protected EnumSetUserType(Class<E> type) {
        this.type = type;
    }

    @Override
    public int[] sqlTypes() {
        return SQL_TYPES;
    }
    
    @Override
    public Class<?> returnedClass() {
        return type;
    }
    
    @Override
    public boolean equals(Object x, Object y) {
        if (x == null) return y == null;
        return x.equals(y);
    }
    
    @Override
    public Object deepCopy(Object value) {
        if (value == null) return null;
        @SuppressWarnings("unchecked")
        final Collection<E> values = Collection.class.cast(value);
        final Set<E> enums = EnumSet.noneOf(type);
        enums.addAll(values);
        return enums;
    }
    
    @Override
    public boolean isMutable() {
        return true;
    }
    
    @Override
    public Object nullSafeGet(ResultSet resultSet, String[] names, Object owner) 
        throws HibernateException, SQLException {
        final long flag = resultSet.getLong(names[0]);
        return Enums.decode(type, flag);
    }
    
    @Override
    public void nullSafeSet(PreparedStatement statement, Object value, int index) 
        throws HibernateException, SQLException {
        if (value == null) {
            statement.setLong(index, 0);
            return;
        }
        @SuppressWarnings("unchecked")
        final Set<E> enums = (Set<E>) value;
        final long flag = Enums.encode(enums);
        statement.setLong(index, flag);
    }
    
    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        @SuppressWarnings("unchecked")
        final Set<E> set = Set.class.cast(cached);
        return cached == null ? null : EnumSet.copyOf(set);
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        @SuppressWarnings("unchecked")
        final Set<E> set = Set.class.cast(value);
        return value == null ? null : EnumSet.copyOf(set);
    }
    
    @Override
    public int hashCode(Object x) throws HibernateException {
        return x.hashCode();
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }

}
