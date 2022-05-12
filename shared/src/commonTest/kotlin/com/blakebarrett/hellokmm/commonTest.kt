package com.blakebarrett.hellokmm

import kotlin.test.Test
import kotlin.test.assertTrue

class CommonGreetingTest : UsingContextTest() {

    @Test
    fun testExample() {
        assertTrue(Greeting().greeting().contains("Hello"), "Check 'Hello' is mentioned")
    }

}