package com.demo.smallutils

import android.app.Application
import com.smallcake.utils.SmallUtils

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        SmallUtils.init(this)
    }
}