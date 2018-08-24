package ru.nikitazhelonkin.coinbalance.utils;


public class ChartColorPallet {

    private static final int[] COLORS = {
            0xffff6859,
            0xffffcf44,
            0xff37efba,
            0xff56d1ca,
            0xff5cceff,
            0xff7986CB,
            0xffa932ff,
    };

    private static final int[] ALPHA = {
            0xffffffff,
            0xc0ffffff,
            0xa0ffffff,
            0x80ffffff,
            0x60ffffff,
            0x40ffffff,
    };

    public static int colorForPosition(int position) {
        position = position % (ALPHA.length * COLORS.length);
        int alphaIdx = position / COLORS.length;
        int colorIdx = position % COLORS.length;
        return COLORS[colorIdx] & ALPHA[alphaIdx];
    }
}
