package com.demo.smallutils

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.smallcake.utils.buildSpannableString
import com.smallcake.utils.showToast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val textView = findViewById<TextView>(R.id.text_view)
        val speedChartView = findViewById<SpeedChartView>(R.id.speed_chart)
        textView.buildSpannableString {
            addText("你好")
            addText("World!"){
                isItalic = true
                isBold = true
                textColor = Color.RED
                onClick{showToast("难道你不觉得中文中夹杂着英文真的很low吗？")}
            }
        }
        speedChartView.setProgress(80f)

    }
}