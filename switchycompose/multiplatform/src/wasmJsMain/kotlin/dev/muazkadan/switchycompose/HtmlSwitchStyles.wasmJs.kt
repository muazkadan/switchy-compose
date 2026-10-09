package dev.muazkadan.switchycompose

internal actual fun isNativeSwitchInputSupported(): Boolean =
    js("'switch' in HTMLInputElement.prototype")
