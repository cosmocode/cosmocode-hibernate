package de.cosmocode.hibernate;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.hibernate.ScrollableResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

/**
 * An adapter which allows using {@link ScrollableResults} as an {@link Iterator}.
 *
 * @author Willi Schoenborn
 * @param <E> the generic element type
 */
public final class ScrollableResultsIterator<E> implements Iterator<E> {

    private static final Logger LOG = LoggerFactory.getLogger(ScrollableResultsIterator.class);

    private final ScrollableResults results;
    
    public ScrollableResultsIterator(ScrollableResults results) {
        this.results = Preconditions.checkNotNull(results, "Results");
    }
    
    @Override
    public boolean hasNext() {
        return !results.isLast();
    }
    
    @Override
    public E next() {
        if (hasNext() && results.next()) {
            @SuppressWarnings("unchecked")
            final E result = (E) results.get(0);
            LOG.trace("Next element is {}", result);
            return result;
        } else {
            throw new NoSuchElementException("No elements left in results");
        }
    };
    
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

}
