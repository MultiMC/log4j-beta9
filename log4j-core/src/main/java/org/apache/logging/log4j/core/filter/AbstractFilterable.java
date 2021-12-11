/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache license, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the license for the specific language governing permissions and
 * limitations under the license.
 */
package org.apache.logging.log4j.core.filter;

import java.util.Iterator;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LifeCycle;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.status.StatusLogger;

/**
 * Enhances a Class by allowing it to contain Filters.
 */
public abstract class AbstractFilterable implements Filterable {

    protected static final Logger LOGGER = StatusLogger.getLogger();

    private volatile Filter filter;

    protected AbstractFilterable(final Filter filter) {
        this.filter = filter;
    }

    protected AbstractFilterable() {
    }

    /**
     * Returns the Filter.
     * @return the Filter.
     */
    @Override
    public Filter getFilter() {
        return filter;
    }

    /**
     * Add a filter.
     * @param filter The Filter to add.
     */
    @Override
    public synchronized void addFilter(final Filter filter) {
        if (this.filter == null) {
            this.filter = filter;
        } else if (filter instanceof CompositeFilter) {
            this.filter = ((CompositeFilter) this.filter).addFilter(filter);
        } else {
            final Filter[] filters = new Filter[] {this.filter, filter};
            this.filter = CompositeFilter.createFilters(filters);
        }
    }

    /**
     * Remove a Filter.
     * @param filter The Filter to remove.
     */
    @Override
    public synchronized void removeFilter(final Filter filter) {
        if (this.filter == filter) {
            this.filter = null;
        } else if (filter instanceof CompositeFilter) {
            CompositeFilter composite = (CompositeFilter) filter;
            composite = composite.removeFilter(filter);
            if (composite.size() > 1) {
                this.filter = composite;
            } else if (composite.size() == 1) {
                final Iterator<Filter> iter = composite.iterator();
                this.filter = iter.next();
            } else {
                this.filter = null;
            }
        }
    }

    /**
     * Determines if a Filter is present.
     * @return false if no Filter is present.
     */
    @Override
    public boolean hasFilter() {
        return filter != null;
    }

    /**
     * Make the Filter available for use.
     */
    public void startFilter() {
       if (filter != null && filter instanceof LifeCycle) {
           ((LifeCycle) filter).start();
       }
    }

    /**
     * Cleanup the Filter.
     */
    public void stopFilter() {
       if (filter != null && filter instanceof LifeCycle) {
           ((LifeCycle) filter).stop();
       }
    }

    /**
     * Determine if the LogEvent should be processed or ignored.
     * @param event The LogEvent.
     * @return true if the LogEvent should be processed.
     */
    @Override
    public boolean isFiltered(final LogEvent event) {
        return filter != null && filter.filter(event) == Filter.Result.DENY;
    }

}
