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
