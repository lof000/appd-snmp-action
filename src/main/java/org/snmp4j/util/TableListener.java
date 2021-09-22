/*
 *   Copyright 2018. AppDynamics LLC and its affiliates.
 *   All Rights Reserved.
 *   This is unpublished proprietary source code of AppDynamics LLC and its affiliates.
 *   The copyright notice above does not evidence any actual or intended publication of such source code.
 *
 */

package org.snmp4j.util;

import java.util.EventListener;

/**
 * The <code>TableListener</code> interface is implemented by objects
 * listening for table events. Table events typically contain row data.
 *
 * @author Frank Fock
 * @version 1.10
 * @since 1.0.2
 * @see org.snmp4j.util.TableUtils
 */
public interface TableListener extends EventListener {

  /**
   * Consumes the next table event, which is typically the next row in a
   * table retrieval operation.
   *
   * @param event
   *    a <code>TableEvent</code> instance.
   * @return
   *    <code>true</code> if this listener wants to receive more events,
   *    otherwise return <code>false</code>. For example, a
   *    <code>TableListener</code> can return <code>false</code> to stop
   *    table retrieval.
   */
  boolean next(TableEvent event);

  /**
   * Indicates in a series of table events that no more events will follow.
   * @param event
   *    a <code>TableEvent</code> instance that will either indicate an error
   *    ({@link org.snmp4j.util.TableEvent#isError()} returns <code>true</code>) or success
   *    of the table operation.
   */
  void finished(TableEvent event);

  /**
   * Indicates whether the tree walk is complete or not.
   * @return
   *    <code>true</code> if it is complete, <code>false</code> otherwise.
   * @since 1.10
   */
  boolean isFinished();

}
