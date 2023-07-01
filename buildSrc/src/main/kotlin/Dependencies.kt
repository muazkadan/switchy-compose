object Versions {
    internal const val KOTLIN_BOM = "1.8.0"
    internal const val ANDROIDX_CORE_KTX = "1.10.1"

    internal const val COMPOSE_BOM = "2023.05.01"
    internal const val COMPOSE_ACTIVITY = "1.7.2"
}

object Dependencies {
    const val androidxCoreKtx = "androidx.core:core-ktx:${Versions.ANDROIDX_CORE_KTX}"
    const val kotlinBom = "org.jetbrains.kotlin:kotlin-bom:${Versions.KOTLIN_BOM}"

    const val composeBom = "androidx.compose:compose-bom:${Versions.COMPOSE_BOM}"
    const val composeUI = "androidx.compose.ui:ui"
    const val composeFoundation = "androidx.compose.foundation:foundation"
    const val composeMaterial = "androidx.compose.material3:material3"
    const val composeTooling = "androidx.compose.ui:ui-tooling"
    const val composeActivity = "androidx.activity:activity-compose:${Versions.COMPOSE_ACTIVITY}"
}