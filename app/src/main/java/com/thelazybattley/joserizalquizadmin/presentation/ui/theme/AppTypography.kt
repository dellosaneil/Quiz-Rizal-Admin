package com.thelazybattley.joserizalquizadmin.presentation.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

fun getAppTypography(): AppTypography {
    val textStyle = TextStyle(
//        fontFamily = getRobotoTypography(),
    )
    return AppTypography(
        regular11 = textStyle.copy(
            fontWeight = FontWeight.Normal,
            fontSize = 11.sp,
        ),
        regular12 = textStyle.copy(
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
        ),
        regular13 = textStyle.copy(
            fontWeight = FontWeight.Normal,
            fontSize = 13.sp
        ),
        regular18 = textStyle.copy(
            fontWeight = FontWeight.Normal,
            fontSize = 18.sp
        ),
        semiBold10 = textStyle.copy(
            fontWeight = FontWeight.SemiBold,
            fontSize = 10.sp,
        ),
        semiBold11 = textStyle.copy(
            fontWeight = FontWeight.SemiBold,
            fontSize = 11.sp,
        ),
        semiBold12 = textStyle.copy(
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
        ),
        semiBold13 = textStyle.copy(
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
        ),
        semiBold14 = textStyle.copy(
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
        ),
        semiBold16 = textStyle.copy(
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        ),
        semiBold17 = textStyle.copy(
            fontWeight = FontWeight.SemiBold,
            fontSize = 17.sp
        ),
        semiBold18 = textStyle.copy(
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp
        ),
        semiBold30 = textStyle.copy(
            fontWeight = FontWeight.SemiBold,
            fontSize = 30.sp
        ),
        semiBold38 = textStyle.copy(
            fontWeight = FontWeight.SemiBold,
            fontSize = 38.sp
        ),
        bold14 = textStyle.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        ),
        bold23 = textStyle.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 23.sp
        ),
        medium10 = textStyle.copy(
            fontWeight = FontWeight.Medium,
            fontSize = 10.sp
        ),
        medium12 = textStyle.copy(
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp
        ),
        medium14 = textStyle.copy(
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp
        ),
        italic12 = textStyle.copy(
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            fontStyle = FontStyle.Italic
        ),
        italic15 = textStyle.copy(
            fontWeight = FontWeight.Normal,
            fontSize = 15.sp,
            fontStyle = FontStyle.Italic
        )
    )
}


data class AppTypography(
    val regular11: TextStyle,
    val regular12: TextStyle,
    val regular13: TextStyle,
    val regular18: TextStyle,
    val semiBold10: TextStyle,
    val semiBold11: TextStyle,
    val semiBold12: TextStyle,
    val semiBold13: TextStyle,
    val semiBold14: TextStyle,
    val semiBold16: TextStyle,
    val semiBold17: TextStyle,
    val semiBold18: TextStyle,
    val semiBold30: TextStyle,
    val semiBold38: TextStyle,
    val bold14: TextStyle,
    val bold23: TextStyle,
    val medium10: TextStyle,
    val medium12: TextStyle,
    val medium14: TextStyle,
    val italic12: TextStyle,
    val italic15: TextStyle,
)

