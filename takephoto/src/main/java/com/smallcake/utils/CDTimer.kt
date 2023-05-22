package com.smallcake.utils

import android.annotation.SuppressLint
import android.os.CountDownTimer
import android.widget.TextView

/**
 * Date:2021/5/28
 * author:SmallCake
 * 60秒倒计时
 * 一般用于点击获取验证码
 * val cbTimer = CDTimer(textView)
 * 点击文本调用接口，收到结果后调用如下方法开始倒计时
 * cbTimer.start()
 **/
class CDTimer: CountDownTimer {
    private  var tv: TextView? = null
    constructor(textView:TextView) : super(60000,1000) {
        this.tv = textView
    }

    @SuppressLint("SetTextI18n")
    override fun onTick(millisUntilFinished: Long) {
        if (tv == null) {
            cancel()
            return
        }
        tv?.text = "${millisUntilFinished /1000}s"
        tv?.isEnabled = false
    }

    override fun onFinish() {
        if (tv == null) {
            cancel()
            return
        }
        tv?.text = "重新获取"
        tv?.isEnabled = true

    }
}

/**
 * 对TextView的扩展
 * val cbTimer = textView.buildCDTimer()
 * 点击文本调用接口，收到结果后调用如下方法开始倒计时
 * cbTimer.start()
 */
fun TextView.buildCDTimer():CDTimer{
    return CDTimer(this)
}
