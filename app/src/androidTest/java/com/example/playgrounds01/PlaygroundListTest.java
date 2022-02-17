package com.example.playgrounds01;

import static org.junit.Assert.*;

import com.example.playgrounds01.Classes.PlaygroundList;

import org.junit.Before;
import org.junit.Test;

public class PlaygroundListTest {

    PlaygroundList plList;

    @Before
    public void setUp() throws Exception {
        plList = new PlaygroundList();
    }

    @Test
    public void calcLengh() {
        assertEquals(17719.746, plList.calcLengh(49.83914601908489, 18.300558874207862, 49.681468050245144, 18.335726288636693), 0.001);
    }
}