package ru.nikitazhelonkin.coinbalance.utils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ChartColorPalletTest {

    @Test
    public void testColors() {
        List<Integer> colors = new ArrayList<>();
        for (int i = 0; i < 120; i++) {
            colors.add(ChartColorPallet.colorForPosition(i));
        }
        assertEquals(120, colors.size());
        assertEquals(42, ListUtils.distinct(colors).size());
    }

}