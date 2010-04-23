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
import java.util.Arrays;
import java.util.Iterator;

import org.easymock.EasyMock;
import org.hibernate.ScrollableResults;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests {@link ScrollableResultsIterator} using {@link MockScrollableResults}.
 *
 * @author Willi Schoenborn
 */
public class ScrollableResultsIteratorTest {

    private <T> Iterator<T> unit(ScrollableResults results) {
        return new ScrollableResultsIterator<T>(results);
    }
    
    private <T> Iterator<T> unit(T... elements) {
        return unit(new MockScrollableResults(Arrays.asList(elements)));
    }
    
    /**
     * Tests a usual iterator usage.
     */
    @Test
    public void test() {
        final Object first = EasyMock.createMock("first", Serializable.class);
        final Object second = EasyMock.createMock("second", Serializable.class);
        final Object third = EasyMock.createMock("third", Serializable.class);
        final Iterator<Object> iterator = unit(first, second, third);
        
        Assert.assertNotNull(iterator);
        Assert.assertTrue(iterator.hasNext());
        Assert.assertSame(first, iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertSame(second, iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertSame(third, iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }
    
}
