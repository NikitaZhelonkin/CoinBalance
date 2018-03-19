package ru.nikitazhelonkin.coinbalance.utils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ColorGeneratorTest {

    @Test
    public void testColors() {
        List<Integer> colors = new ArrayList<>();
        for (int i = 0; i < 120; i++) {
            colors.add(ColorGenerator.colorForPosition(i));
        }
        assertEquals(120, colors.size());
        assertEquals(60, ListUtils.distinct(colors).size());
    }

}