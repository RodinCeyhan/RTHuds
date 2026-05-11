package com.rtc.client.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"unused", "SpellCheckingInspection"})
public class Log {

    private static final Logger LOGGER = LoggerFactory.getLogger("RTHuds");

    public static void info(String msg, Object... args) {
        LOGGER.info(msg, args);
    }

    public static void warn(String msg, Object... args) {
        LOGGER.warn(msg, args);
    }

    public static void error(String msg, Object... args) {
        LOGGER.error(msg, args);
    }

    public static void debug(String msg, Object... args) {
        LOGGER.debug(msg, args);
    }
}