package com.blakebarrett.hellokmm

import swift.Bridge.StaticLibrary

actual class Platform actual constructor() {
    actual val platform: String = StaticLibrary.test()
}