package util

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.tween

fun <T> tweenSpec(duration: Int = 500, easing: Easing = LinearOutSlowInEasing): TweenSpec<T> =
    tween(durationMillis = duration, easing = easing)