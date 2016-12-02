/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.common.logger

/**
  * Helper class which wraps Android's native Log utility in the Logger interface.  This way
  * normal DDMS output can be one of the many targets receiving and outputting logs simultaneously.
  */
class LogWrapper extends LogNode {

  // For piping:  The next node to receive Log data after this one has done its work.
  private var mNext: LogNode = null

  /**
    * Returns the next LogNode in the linked list.
    */
  def getNext: LogNode = mNext

  /**
    * Sets the LogNode data will be sent to..
    */
  def setNext(node: LogNode) {
    mNext = node
  }

  /**
    * Prints data out to the console using Android's native log mechanism.
    *
    * @param priority Log level of the data being logged.  Verbose, Error, etc.
    * @param tag      Tag for for the log data.  Can be used to organize log statements.
    * @param msg      The actual message to be logged. The actual message to be logged.
    * @param tr       If an exception was thrown, this can be sent along for the logging facilities
    *                 to extract and print useful information.
    */
  override def println(priority: Int, tag: String, msg: String, tr: Throwable) {
    var useMsg: String = msg
    if (useMsg == null) {
      useMsg = ""
    }
    val mmsg =
      if (tr != null) {
        msg + "\n" + android.util.Log.getStackTraceString(tr)
      } else msg
    android.util.Log.println(priority, tag, useMsg)
    if (mNext != null) {
      mNext.println(priority, tag, mmsg, tr)
    }
  }
}
