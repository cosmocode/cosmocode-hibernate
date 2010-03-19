package de.cosmocode.hibernate;

import java.util.Iterator;

import org.easymock.EasyMock;
import org.hibernate.ScrollableResults;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScrollableResultsIteratorTest {

    private static final Logger LOG = LoggerFactory.getLogger(ScrollableResultsIteratorTest.class);

    private Iterator<?> unit(ScrollableResults results) {
        return new ScrollableResultsIterator<Object>(results);
    }
    
    @Test
    public void next() {
        final ScrollableResults results = EasyMock.createMock(ScrollableResults.class);
        final Object first = new Object();
        EasyMock.expect(results.isLast()).andStubReturn(false);
        EasyMock.expect(results.next()).andReturn(true);
        EasyMock.expect(results.get(0)).andReturn(first);
        EasyMock.replay(results);
        final Iterator<?> iterator = unit(results);
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(first, iterator.next());
    }
    
}
