package com.quickgame.circlechallenge.utils

import androidx.compose.ui.geometry.Rect
import com.quickgame.circlechallenge.R

object Constants {
    const val TIME_OUT = 10000L
    const val TIME_OUT_MIN = 1000L
    const val TIME_OUT_MAX = 3000L

    fun listImageAvatars(): List<Int> {
        return listOf(
            R.drawable.img_1,
            R.drawable.img_2,
            R.drawable.img_3,
            R.drawable.img_4,
            R.drawable.img_5,
            R.drawable.img_6,
            R.drawable.img_7,
            R.drawable.img_8,
            R.drawable.img_9,
            R.drawable.img_10,
            R.drawable.img_11,
            R.drawable.img_12,
            R.drawable.img_13,
            R.drawable.img_14,
            R.drawable.img_15,
            R.drawable.img_16,
            R.drawable.img_17,
            R.drawable.img_18,
            R.drawable.img_19,
            R.drawable.img_20,
        )
    }

    // Function to check if two rectangles intersect
    fun rectIntersects(rect1: Rect, rect2: Rect): Boolean {
        return (rect1.left < rect2.right && rect1.right > rect2.left
                && rect1.top < rect2.bottom && rect1.bottom > rect2.top)
    }
}

