package com.rtc.client.huds;

public final class ChromaUtil {
    private ChromaUtil() {}

    private static final long CYCLE_MS = 6000;

    public static int color() {
        long t = System.currentTimeMillis() % CYCLE_MS;
        float hue = (float) t / CYCLE_MS;
        int rgb = java.awt.Color.HSBtoRGB(hue, 1f, 1f) & 0xFFFFFF;
        return 0xFF000000 | rgb;
    }
}