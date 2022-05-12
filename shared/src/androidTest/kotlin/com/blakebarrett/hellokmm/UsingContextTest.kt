package com.blakebarrett.hellokmm

import android.os.Build
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
@Config(manifest= Config.NONE)
actual abstract class UsingContextTest {

    init {
        println("test is executed on sdk ${Build.VERSION.SDK_INT}")
    }

}