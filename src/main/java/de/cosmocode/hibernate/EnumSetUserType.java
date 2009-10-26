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

import de.cosmocode.commons.EnumUtility;

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
    @SuppressWarnings("unchecked")
    public Object deepCopy(Object value) {
        if (value == null) return null;
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
    public Object nullSafeGet(ResultSet resultSet, String[] names, Object owner) throws HibernateException, SQLException {
        final long flag = resultSet.getLong(names[0]);
        return EnumUtility.decode(type, flag);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public void nullSafeSet(PreparedStatement statement, Object value, int index) throws HibernateException, SQLException {
        if (value == null) {
            statement.setLong(index, 0);
            return;
        }
        final Set<E> enums = (Set<E>) value;
        final long flag = EnumUtility.encode(enums);
        statement.setLong(index, flag);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached == null ? null : EnumSet.copyOf((Set<E>) cached);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Serializable disassemble(Object value) throws HibernateException {
        return value == null ? null : EnumSet.copyOf((Set<E>) value);
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