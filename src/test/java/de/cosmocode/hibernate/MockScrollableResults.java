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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.Clob;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.hibernate.HibernateException;
import org.hibernate.ScrollableResults;
import org.hibernate.type.Type;

import com.google.common.base.Preconditions;

/**
 * Mock implementation of the {@link ScrollableResults} which uses
 * an underlying list.
 *
 * @author Willi Schoenborn
 */
final class MockScrollableResults implements ScrollableResults {

    private final List<?> list;
    
    private int index = -1;
    
    public <T> MockScrollableResults(List<T> list) {
        this.list = Preconditions.checkNotNull(list, "List");
    }
    
    @Override
    public void afterLast() throws HibernateException {
        index = list.size();
    }

    @Override
    public void beforeFirst() throws HibernateException {
        index = -1;
    }

    @Override
    public void close() throws HibernateException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean first() throws HibernateException {
        index = 0;
        return list.size() > 0;
    }

    @Override
    public Object get(int i) throws HibernateException {
        return list.get(index);
    }

    @Override
    public boolean isFirst() throws HibernateException {
        return index == 0;
    }

    @Override
    public boolean isLast() throws HibernateException {
        return index == list.size() - 1;
    }

    @Override
    public boolean last() throws HibernateException {
        index = list.size() - 1;
        return list.size() > 0;
    }

    @Override
    public boolean next() throws HibernateException {
        index++;
        return index < list.size();
    }

    @Override
    public boolean previous() throws HibernateException {
        index -= 1;
        return index < list.size();
    }

    @Override
    public boolean scroll(int i) throws HibernateException {
        index += i;
        return index < list.size();
    }

    @Override
    public int getRowNumber() throws HibernateException {
        return index;
    }

    @Override
    public boolean setRowNumber(int rowNumber) throws HibernateException {
        index = rowNumber;
        return index < list.size();
    }

    @Override
    public Object[] get() throws HibernateException {
        throw new UnsupportedOperationException();
    }

    @Override
    public BigDecimal getBigDecimal(int col) throws HibernateException {
        throw new UnsupportedOperationException();
    }

    @Override
    public BigInteger getBigInteger(int col) throws HibernateException {
        throw new UnsupportedOperationException();
    }

    @Override
    public byte[] getBinary(int col) throws HibernateException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Blob getBlob(int col) throws HibernateException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Boolean getBoolean(int col) throws HibernateException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Byte getByte(int col) throws HibernateException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Calendar getCalendar(int col) throws HibernateException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Character getCharacter(int col) throws HibernateException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Clob getClob(int col) throws HibernateException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Date getDate(int col) throws HibernateException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Double getDouble(int col) throws HibernateException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Float getFloat(int col) throws HibernateException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Integer getInteger(int col) throws HibernateException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Locale getLocale(int col) throws HibernateException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Long getLong(int col) throws HibernateException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Short getShort(int col) throws HibernateException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getString(int col) throws HibernateException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getText(int col) throws HibernateException {
        throw new UnsupportedOperationException();
    }

    @Override
    public TimeZone getTimeZone(int col) throws HibernateException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Type getType(int i) {
        throw new UnsupportedOperationException();
    }

}
