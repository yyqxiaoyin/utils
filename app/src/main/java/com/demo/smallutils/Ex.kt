package com.demo.smallutils

import com.smallcake.utils.SmallUtils
import java.math.BigDecimal

/**
 * dp 转 px
 * @receiver Int 1
 * @return Int 3
 */
val Int.px: Int
    get() {
        val scale = SmallUtils.context?.resources
            ?.displayMetrics?.density ?: 0.0f
        return (this * scale + 0.5f).toInt()
    }
val Float.px: Float
    get() {
        val scale = SmallUtils.context?.resources
            ?.displayMetrics?.density ?: 0.0f
        return BigDecimal((this * scale + 0.5f).toString()).setScale(2, BigDecimal.ROUND_HALF_UP)
            .toFloat()
    }