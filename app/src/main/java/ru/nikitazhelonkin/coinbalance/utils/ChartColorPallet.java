package ru.nikitazhelonkin.coinbalance.utils;


public class ChartColorPallet {

    private static final int[] COLORS = {
            0xfffd7773,
            0xfffec071,
            0xffdffd74,
            0xff72fdac,
            0xff72fffe,
            0xff71acfc,
            0xff8b72fb,
            0xffd877f7,
            0xfffee5fc,
            0xfff97875,
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
        position = position % 60;
        int alphaIdx = position / 10;
        int colorIdx = position % 10;
        return COLORS[colorIdx] & ALPHA[alphaIdx];
    }
}
