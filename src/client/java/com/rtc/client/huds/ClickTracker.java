package com.rtc.client.huds;

import java.util.ArrayDeque;
import java.util.Deque;

public final class ClickTracker {
    public static final Deque<Long> LEFT = new ArrayDeque<>();
    public static final Deque<Long> RIGHT = new ArrayDeque<>();

    private ClickTracker() {}

    public static int cps(Deque<Long> q) {
        long now = System.currentTimeMillis();
        while (!q.isEmpty() && now - q.peekFirst() > 1000) q.removeFirst();
        return q.size();
    }
}