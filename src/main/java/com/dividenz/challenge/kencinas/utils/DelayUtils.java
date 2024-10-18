package com.dividenz.challenge.kencinas.utils;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

public final class DelayUtils {
    /**
     * Gets a random duration in seconds.
     *
     * @param start: start point
     * @param end: end point
     * @return duration in seconds
     */
    public static Duration getRandomSeconds(long start, long end) {
        return Duration.ofSeconds(ThreadLocalRandom.current().nextLong(start, end));
    }
}
