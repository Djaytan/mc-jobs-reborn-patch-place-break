/*-
 * #%L
 * JobsReborn-PatchPlaceBreak
 * %%
 * Copyright (C) 2022 - 2023 Loïc DUBOIS-TERMOZ (alias Djaytan)
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.bukkit.slf4j;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import org.slf4j.Marker;
import org.slf4j.helpers.AbstractLogger;
import org.slf4j.helpers.LegacyAbstractLogger;
import org.slf4j.helpers.MessageFormatter;
import org.slf4j.helpers.NormalizedParameters;
import org.slf4j.helpers.SubstituteLogger;
import org.slf4j.spi.DefaultLoggingEventBuilder;
import org.slf4j.spi.LocationAwareLogger;

/**
 * This class is highly inspired from slf4j-jdk14 module.
 *
 * <p>The purpose is to permit the creation of a SLF4J instance of the Bukkit logger to ease the
 * decoupling.
 */
final class BukkitLoggerAdapter extends LegacyAbstractLogger implements LocationAwareLogger {

  private static final String SELF = BukkitLoggerAdapter.class.getName();
  private static final String SUPER = LegacyAbstractLogger.class.getName();
  private static final String SUPER_OF_SUPER = AbstractLogger.class.getName();
  private static final String SUBSTITUTE = SubstituteLogger.class.getName();
  private static final String FLUENT = DefaultLoggingEventBuilder.class.getName();
  private static final String[] BARRIER_CLASSES =
      new String[] {SUPER_OF_SUPER, SUPER, SELF, SUBSTITUTE, FLUENT};

  final transient Logger logger;

  BukkitLoggerAdapter(Logger logger) {
    this.logger = logger;
    this.name = logger.getName();
  }

  /**
   * Is this logger instance enabled for the FINEST level?
   *
   * @return True if this Logger is enabled for level FINEST, false otherwise.
   */
  public boolean isTraceEnabled() {
    return logger.isLoggable(Level.FINEST);
  }

  /**
   * Is this logger instance enabled for the FINE level?
   *
   * @return True if this Logger is enabled for level FINE, false otherwise.
   */
  public boolean isDebugEnabled() {
    return logger.isLoggable(Level.FINE);
  }

  /**
   * Is this logger instance enabled for the INFO level?
   *
   * @return True if this Logger is enabled for the INFO level, false otherwise.
   */
  public boolean isInfoEnabled() {
    return logger.isLoggable(Level.INFO);
  }

  /**
   * Is this logger instance enabled for the WARNING level?
   *
   * @return True if this Logger is enabled for the WARNING level, false otherwise.
   */
  public boolean isWarnEnabled() {
    return logger.isLoggable(Level.WARNING);
  }

  /**
   * Is this logger instance enabled for level SEVERE?
   *
   * @return True if this Logger is enabled for level SEVERE, false otherwise.
   */
  public boolean isErrorEnabled() {
    return logger.isLoggable(Level.SEVERE);
  }

  /**
   * Log the message at the specified level with the specified throwable if any. This method creates
   * a LogRecord and fills in caller date before calling this instance's JDK14 logger.
   */
  @Override
  protected void handleNormalizedLoggingCall(
      org.slf4j.event.Level level, Marker marker, String msg, Object[] args, Throwable throwable) {
    innerNormalizedLoggingCallHandler(getFullyQualifiedCallerName(), level, msg, args, throwable);
  }

  private void innerNormalizedLoggingCallHandler(
      String fqcn, org.slf4j.event.Level level, String msg, Object[] args, Throwable throwable) {
    // millis and thread are filled by the constructor
    Level julLevel = slf4jLevelToJULLevel(level);
    String formattedMessage = MessageFormatter.basicArrayFormat(msg, args);
    LogRecord logRecord = new LogRecord(julLevel, formattedMessage);

    // https://jira.qos.ch/browse/SLF4J-13
    logRecord.setLoggerName(getName());
    logRecord.setThrown(throwable);
    // Note: parameters in record are not set because SLF4J only
    // supports a single formatting style
    // See also https://jira.qos.ch/browse/SLF4J-10
    fillCallerData(fqcn, logRecord);
    logger.log(logRecord);
  }

  @Override
  protected String getFullyQualifiedCallerName() {
    return SELF;
  }

  @Override
  public void log(
      Marker marker,
      String callerFQCN,
      int slf4jLevelInt,
      String message,
      Object[] arguments,
      Throwable throwable) {

    org.slf4j.event.Level slf4jLevel = org.slf4j.event.Level.intToLevel(slf4jLevelInt);
    Level julLevel = slf4jLevelIntToJULLevel(slf4jLevelInt);

    if (logger.isLoggable(julLevel)) {
      NormalizedParameters np = NormalizedParameters.normalize(message, arguments, throwable);
      innerNormalizedLoggingCallHandler(
          callerFQCN, slf4jLevel, np.getMessage(), np.getArguments(), np.getThrowable());
    }
  }

  /**
   * Fill in caller data if possible.
   *
   * @param logRecord The record to update
   */
  private void fillCallerData(String callerFQCN, LogRecord logRecord) {
    StackTraceElement[] steArray = new Throwable().getStackTrace();

    int selfIndex = -1;
    for (int i = 0; i < steArray.length; i++) {
      final String className = steArray[i].getClassName();

      if (barrierMatch(callerFQCN, className)) {
        selfIndex = i;
        break;
      }
    }

    int found = -1;
    for (int i = selfIndex + 1; i < steArray.length; i++) {
      final String className = steArray[i].getClassName();
      if (!(barrierMatch(callerFQCN, className))) {
        found = i;
        break;
      }
    }

    if (found != -1) {
      StackTraceElement ste = steArray[found];
      // setting the class name has the side effect of setting
      // the needToInferCaller variable to false.
      logRecord.setSourceClassName(ste.getClassName());
      logRecord.setSourceMethodName(ste.getMethodName());
    }
  }

  private boolean barrierMatch(String callerFQCN, String candidateClassName) {
    if (candidateClassName.equals(callerFQCN)) {
      return true;
    }
    for (String barrierClassName : BARRIER_CLASSES) {
      if (barrierClassName.equals(candidateClassName)) {
        return true;
      }
    }
    return false;
  }

  private static Level slf4jLevelIntToJULLevel(int levelInt) {
    org.slf4j.event.Level slf4jLevel = org.slf4j.event.Level.intToLevel(levelInt);
    return slf4jLevelToJULLevel(slf4jLevel);
  }

  private static Level slf4jLevelToJULLevel(org.slf4j.event.Level slf4jLevel) {
    Level julLevel;
    switch (slf4jLevel) {
      case TRACE:
        julLevel = Level.FINEST;
        break;
      case DEBUG:
        julLevel = Level.FINE;
        break;
      case INFO:
        julLevel = Level.INFO;
        break;
      case WARN:
        julLevel = Level.WARNING;
        break;
      case ERROR:
        julLevel = Level.SEVERE;
        break;
      default:
        throw new IllegalStateException("Level " + slf4jLevel + " is not recognized.");
    }
    return julLevel;
  }
}
