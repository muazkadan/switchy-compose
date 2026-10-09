package dev.muazkadan.switchycompose

/**
 * Marks Switchy Compose APIs that are experimental: they may change or be removed in future
 * releases without a deprecation cycle.
 */
@RequiresOptIn(
    message = "This Switchy Compose API is experimental and may change or be removed in the future.",
    level = RequiresOptIn.Level.WARNING,
)
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
annotation class ExperimentalSwitchyApi
