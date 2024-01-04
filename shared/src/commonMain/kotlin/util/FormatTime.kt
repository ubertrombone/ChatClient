package util

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

fun Instant.ago() =
    Clock.System.now().minus(this).toComponents { days, hours, minutes, seconds, _ ->
        when {
            days > 0 -> return@toComponents "$days ${"day".pluralizeTime(days.toInt())} ago"
            hours > 0 -> return@toComponents "$hours ${"hour".pluralizeTime(hours)} ago"
            minutes > 0 -> return@toComponents "$minutes ${"minute".pluralizeTime(minutes)} ago"
            else -> return@toComponents "$seconds ${"second".pluralizeTime(seconds)} ago"
        }
    }

private fun String.pluralizeTime(count: Int) = takeIf { count <= 1 } ?: "${this}s"