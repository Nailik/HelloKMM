package com.blakebarrett.hellokmm

import kotlin.test.Test
import kotlin.test.assertTrue

class CommonGreetingTest {

    @Test
    fun testExample() {
        //for ios
        assertTrue(Platform().platform == "testWorked!")
    }

}