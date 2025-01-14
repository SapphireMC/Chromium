/*
 * Copyright (c) 2025 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package me.denarydev.chromium.util;

import net.minecraft.util.math.MathHelper;

/**
 * @author DenaryDev
 * @since 19:54 14.12.2023
 */
public final class ColorUtils {
    public static final int PING_START = 0;
    public static final int PING_MID = 150;
    public static final int PING_END = 300;

    public static final int COLOR_GREY = 0x535353;
    public static final int COLOR_START = 0x00E676;
    public static final int COLOR_MID = 0xD6CD30;
    public static final int COLOR_END = 0xE53935;

    public static int getColor(int ping) {
        if (ping < PING_START) {
            return COLOR_GREY;
        } else if (ping < PING_MID) {
            return interpolate(COLOR_START, COLOR_MID, computeOffset(PING_START, PING_MID, ping));
        } else {
            return interpolate(COLOR_MID, COLOR_END, computeOffset(PING_MID, PING_END, Math.min(ping, PING_END)));
        }
    }

    public static int fromHex(String hex) {
        return Integer.parseInt(hex.substring(1), 16);
    }

    public static String toHex(int color) {
        return '#' + Integer.toHexString(color);
    }

    private static float computeOffset(int start, int end, int value) {
        final float offset = (value - start) / (float) (end - start);
        return MathHelper.clamp(offset, 0.0F, 1.0F);
    }

    public static int interpolate(int start, int end, float offset) {
        if (offset < 0 || offset > 1) {
            throw new IllegalArgumentException("Offset must be between 0.0 and 1.0");
        }

        final int redDiff = getRed(end) - getRed(start);
        final int greenDiff = getGreen(end) - getGreen(start);
        final int blueDiff = getBlue(end) - getBlue(start);

        final int newRed = Math.round(getRed(start) + (redDiff * offset));
        final int newGreen = Math.round(getGreen(start) + (greenDiff * offset));
        final int newBlue = Math.round(getBlue(start) + (blueDiff * offset));

        return (newRed << 16) | (newGreen << 8) | newBlue;
    }

    private static int getRed(int color) {
        return (color >> 16) & 0xFF;
    }

    private static int getGreen(int color) {
        return (color >> 8) & 0xFF;
    }

    private static int getBlue(int color) {
        return color & 0xFF;
    }
}
