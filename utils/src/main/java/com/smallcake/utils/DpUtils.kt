package com.smallcake.utils
import java.math.BigDecimal

/**
 * 单位转换
 * dp 转 px 一般用于在代码中写xml中对应宽度
 * @receiver Int 1
 * @return Int 3
 */
val Int.px: Int
    get() {
        val scale = SmallUtils.context?.applicationContext?.resources
            ?.displayMetrics?.density ?: 0.0f
        return (this * scale + 0.5f).toInt()
    }
val Float.px: Float
    get() {
        val scale = SmallUtils.context?.applicationContext?.resources
            ?.displayMetrics?.density ?: 0.0f
        return BigDecimal((this * scale + 0.5f).toString()).setScale(2, BigDecimal.ROUND_HALF_UP)
            .toFloat()
    }
