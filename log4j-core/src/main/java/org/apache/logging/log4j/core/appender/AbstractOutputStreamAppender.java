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
package org.apache.logging.log4j.core.appender;

import java.io.Serializable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;

/**
 * Appends log events as bytes to a byte output stream. The stream encoding is defined in the layout.
 */
public abstract class AbstractOutputStreamAppender extends AbstractAppender {

    /**
     * Immediate flush means that the underlying writer or output stream
     * will be flushed at the end of each append operation. Immediate
     * flush is slower but ensures that each append request is actually
     * written. If <code>immediateFlush</code> is set to
     * {@code false}, then there is a good chance that the last few
     * logs events are not actually written to persistent media if and
     * when the application crashes.
     */
    protected final boolean immediateFlush;

    private volatile OutputStreamManager manager;

    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final Lock readLock = rwLock.readLock();
    private final Lock writeLock = rwLock.writeLock();

    /**
     * Instantiate a WriterAppender and set the output destination to a
     * new {@link java.io.OutputStreamWriter} initialized with <code>os</code>
     * as its {@link java.io.OutputStream}.
     * @param name The name of the Appender.
     * @param layout The layout to format the message.
     * @param manager The OutputStreamManager.
     */
    protected AbstractOutputStreamAppender(final String name, final Layout<? extends Serializable> layout, final Filter filter,
                                           final boolean ignoreExceptions, final boolean immediateFlush,
                                           final OutputStreamManager manager) {
        super(name, filter, layout, ignoreExceptions);
        this.manager = manager;
        this.immediateFlush = immediateFlush;
    }

    protected OutputStreamManager getManager() {
        return manager;
    }

    protected void replaceManager(final OutputStreamManager newManager) {

        writeLock.lock();
        try {
            final OutputStreamManager old = manager;
            manager = newManager;
            old.release();
        } finally {
            writeLock.unlock();
        }

    }

    @Override
    public void start() {
        if (getLayout() == null) {
            LOGGER.error("No layout set for the appender named [" + getName() + "].");
        }
        if (manager == null) {
            LOGGER.error("No OutputStreamManager set for the appender named [" + getName() + "].");
        }
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
        manager.release();
    }

    /**
     * Actual writing occurs here.
     * <p/>
     * <p>Most subclasses of <code>AbstractOutputStreamAppender</code> will need to
     * override this method.
     * @param event The LogEvent.
     */
    @Override
    public void append(final LogEvent event) {
        readLock.lock();
        try {
            final byte[] bytes = getLayout().toByteArray(event);
            if (bytes.length > 0) {
                manager.write(bytes);
                if (this.immediateFlush || event.isEndOfBatch()) {
                    manager.flush();
                }
            }
        } catch (final AppenderLoggingException ex) {
            error("Unable to write to stream " + manager.getName() + " for appender " + getName());
            throw ex;
        } finally {
            readLock.unlock();
        }
    }
}
