package com.thelazybattley.joserizalquizadmin.presentation.ui.theme

import androidx.compose.ui.graphics.Color

data class AppColors(
    val woodsmokeBrown: Color,
    val warmIvory: Color,
    val espresso: Color,
    val maroon: Color,
    val white: Color,
    val taupe: Color,
    val opacity20White: Color,
    val parchment: Color,
    val antiqueGold: Color,
    val ivoryMist: Color,
    val brickRed: Color,
    val warmLinen: Color,
    val antiqueCream: Color,
    val deepMoss: Color,
    val softSage: Color,
    val softBlush: Color
)


val lightAppColors = AppColors(
    woodsmokeBrown = WoodsmokeBrown,
    warmIvory = WarmIvory,
    espresso = Espresso,
    white = White,
    maroon = Maroon,
    taupe = Taupe,
    opacity20White = White.copy(alpha = 0.2f),
    parchment = Parchment,
    antiqueGold = AntiqueGold,
    ivoryMist = IvoryMist,
    brickRed = BrickRed,
    warmLinen = WarmLinen,
    antiqueCream = AntiqueCream,
    deepMoss = DeepMoss,
    softSage = SoftSage,
    softBlush = SoftBlush
)

val darkAppColors = AppColors(
    woodsmokeBrown = DarkWoodSmokeBrown,
    warmIvory = DarkWarmIvory,
    espresso = DarkEspresso,
    white = DarkWhite,
    maroon = DarkMaroon,
    taupe = DarkTaupe,
    opacity20White = White.copy(alpha = 0.2f),
    parchment = DarkParchment,
    antiqueGold = DarkAntiqueGold,
    ivoryMist = DarkIvoryMist,
    brickRed = DarkBrickRed,
    warmLinen = DarkWarmLinen,
    antiqueCream = DarkAntiqueCream,
    deepMoss = DarkDeepMoss,
    softSage = DarkSoftSage,
    softBlush = DarkSoftBlush
)
