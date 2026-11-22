package com.rtc.client.huds;

import java.text.DecimalFormat;

public final class CoordFormatter {
    public static final int DEFAULT = 6;
    public static final int MIN = 0;
    public static final int MAX = 6;

    private int decimalPlaces = DEFAULT;
    private int cachedPlaces = -1;
    private DecimalFormat dfX, dfY, dfZ;

    public int getDecimalPlaces() { return decimalPlaces; }

    @SuppressWarnings("MathClampMigration")
    public void setDecimalPlaces(int places) {
        this.decimalPlaces = Math.max(MIN, Math.min(MAX, places));
    }

    private void ensure() {
        if (cachedPlaces == decimalPlaces && dfX != null) return;
        int xz, y;
        if (decimalPlaces == DEFAULT) {
            xz = 3;
            y = 6;
        } else {
            xz = decimalPlaces;
            y = decimalPlaces;
        }
        dfX = build(xz);
        dfZ = build(xz);
        dfY = build(y);
        cachedPlaces = decimalPlaces;
    }

    private static DecimalFormat build(int places) {
        StringBuilder pattern = new StringBuilder("0");
        if (places > 0) {
            pattern.append(".");
            pattern.repeat("0", places);
        }
        return new DecimalFormat(pattern.toString());
    }

    public String x(double v) { ensure(); return dfX.format(v); }
    public String y(double v) { ensure(); return dfY.format(v); }
    public String z(double v) { ensure(); return dfZ.format(v); }
}