package com.smallcake.utils

import android.graphics.*
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.text.Layout
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.*
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes


/**
 * 如何使用
textView.buildSpannableString {
    addText("你好")
    addText("World!"){
        isItalic = true
        isBold = true
        textColor = Color.RED
        onClick{showToast("难道你不觉得中文中夹杂着英文真的很low吗？")}
    }
}
 */
//为TextView添加富文本扩展函数
fun TextView.buildSpannableString(onClickEnable:Boolean=false,init:DslSpannableStringBuilderImpl.()->Unit){
    //具体实现类
    val spanStringBuilderImpl = DslSpannableStringBuilderImpl()
    spanStringBuilderImpl.init()
    //此方法在需要响应用户事件时使用
    if (onClickEnable)movementMethod = LinkMovementMethod.getInstance()
    //通过实现类返回SpannableStringBuilder
    text = spanStringBuilderImpl.build()
}
/**
 * 使用DSL扩展来实现富文本
 * 1.它是TextView的一个扩展函数
 * 2.它的内部是 DSL 风格的代码
 * 3.它的每段文字都有设置颜色 & 点击事件的函数
 *
 * 包含：
 * 三个实现类
 * @see DslSpannableStringBuilderImpl
 * @see DslSpanBuilderImpl
 *
 * 参考：
 * https://blog.csdn.net/benhuo931115/article/details/51069373
 *
 */
class DslSpannableStringBuilderImpl  {
    private val builder = SpannableStringBuilder()
    //记录上次添加文字后最后的索引值
    private var lastIndex: Int = 0

    fun addText(text: String, method: (DslSpanBuilderImpl.() -> Unit)?=null) {
        val start = lastIndex
        builder.append(text)
        lastIndex += text.length
        val spanBuilder = DslSpanBuilderImpl()
        method?.let { spanBuilder.it() }
        spanBuilder.apply {
            //默认超链接也不显示下划线
            onClickSpan?.let {
                builder.setSpan(it, start, lastIndex, flag)
                if (!isUnderLine) {builder.setSpan(object : UnderlineSpan() {
                    override fun updateDrawState(ds: TextPaint) {
                        ds.color = ds.linkColor
                        ds.isUnderlineText = false
                    }}, start, lastIndex, flag)}
            }
            //超连接
            linkUrl?.let {
                builder.setSpan(URLSpan(it), start, lastIndex, flag)
                if (!isUnderLine) {builder.setSpan(object : UnderlineSpan() {
                    override fun updateDrawState(ds: TextPaint) {
                        ds.color = ds.linkColor
                        ds.isUnderlineText = false
                    }}, start, lastIndex, flag)}
            }

            //前景色,背景色，圆角背景色，引用线颜色
            textColor?.let {builder.setSpan(ForegroundColorSpan(it), start, lastIndex, flag)}
            backgroundColor?.let {builder.setSpan(BackgroundColorSpan(it), start, lastIndex, flag)}
            radiusBgColorSpan?.let {builder.setSpan(it, start, lastIndex, flag)}
            quoteSpan?.let {builder.setSpan(it, start, lastIndex, flag)}

            //缩进
            leadingMarginSpan?.let { builder.setSpan(it, start, lastIndex, flag) }
            //列表标记
            bulletSpan?.let { builder.setSpan(it, start, lastIndex, flag) }
            //删除线，下划线，上标，下标
            if (isDelLine) builder.setSpan(StrikethroughSpan(), start, lastIndex, flag)
            if (isUnderLine) builder.setSpan(UnderlineSpan(), start, lastIndex, flag)
            if (isTopTag) builder.setSpan(SuperscriptSpan(), start, lastIndex, flag)
            if (isBottomTag) builder.setSpan(SubscriptSpan(), start, lastIndex, flag)

            //缩小与放大
            if (scale != -1f) builder.setSpan(RelativeSizeSpan(scale), start, lastIndex, flag)
            if (scaleX != -1f) builder.setSpan(ScaleXSpan(scaleX), start, lastIndex, flag)
            //粗体，斜体，字体,对其方式,模糊
            if (isBold)builder.setSpan(StyleSpan(Typeface.BOLD), start, lastIndex, flag)
            if (isItalic)builder.setSpan(StyleSpan(Typeface.ITALIC), start, lastIndex, flag)
            fontFamily?.let { builder.setSpan(TypefaceSpan(fontFamily), start, lastIndex, flag) }
            align?.let { builder.setSpan(AlignmentSpan.Standard(it), start, lastIndex, flag) }
            blurFilter?.let { builder.setSpan(MaskFilterSpan(it), start, lastIndex, flag) }
            //图片
            bitmap?.let { builder.setSpan(SmallUtils.context?.let { it1 -> ImageSpan(it1,it) }, start, lastIndex, flag) }
            drawable?.let { builder.setSpan(ImageSpan(it), start, lastIndex, flag) }
            uri?.let { builder.setSpan(SmallUtils.context?.let { it1 -> ImageSpan(it1,it) }, start, lastIndex, flag) }
            resourceId?.let { builder.setSpan(SmallUtils.context?.let { it1 -> ImageSpan(it1,it) }, start, lastIndex, flag) }


        }
    }

    fun build(): SpannableStringBuilder {
        return builder
    }
}
class DslSpanBuilderImpl  {
    /**
     * 文本标记
     * 它是用来标识在 Span 范围内的文本前后输入新的字符时是否把它们也应用这个效果
     * @see Spanned.SPAN_EXCLUSIVE_EXCLUSIVE(前后都不包括)、
     * @see Spanned.SPAN_INCLUSIVE_EXCLUSIVE(前面包括，后面不包括)、
     * @see Spanned.SPAN_EXCLUSIVE_INCLUSIVE(前面不包括，后面包括)、
     * @see Spanned.SPAN_INCLUSIVE_INCLUSIVE(前后都包括)
     */
    var flag: Int = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    /**
     * 前景色：文本颜色
     */
    @ColorInt
    var textColor:Int?=null
    /**
     * 文本背景颜色
     */
    @ColorInt
    var backgroundColor: Int? = null
    /**
     * 圆角背景色
     */
    var radiusBgColorSpan: RadiusBackgroundSpan? = null
    /**
     * 引用线条色:会添加文字前面的一条竖线
     */
    var quoteSpan: QuoteSpan? = null
    /**
     * 列表标记
     */
    var bulletSpan: BulletSpan? = null
    /**
     * 点击事件
     */
    var onClickSpan: ClickableSpan? = null
    /**
     * 缩进
     */
    var leadingMarginSpan: LeadingMarginSpan? = null
    /**
     * 是否需要下划线
     */
    var isUnderLine = false
    /**
     * 是否需要删除线
     */
    var isDelLine = false
    /**
     * 是否需要上标
     * 效果：文字移动到上面
     * 从中线开始上移
     */
    var isTopTag = false
    /**
     * 是否需要下标
     * 效果：文字移动到下面
     * 从最下面开始下移
     */
    var isBottomTag = false

    /**
     * 整体缩放比例 0.5f（缩小到原来的一半） 2f（放大到原来的一倍）
     */
    var scale: Float =-1f
    /**
     * X轴方向缩放比例 0.5f（缩小到原来的一半） 2f（放大到原来的一倍）
     * 效果：变扁
     */
    var scaleX: Float =-1f

    /**
     * 粗体
     */
    var isBold = false
    /**
     * 斜体
     */
    var isItalic = false
    /**
     * 字体
     */
    var fontFamily: String? = null
    /**
     * 对齐方式
     * [Layout.Alignment.ALIGN_NORMAL]正常
     * [Layout.Alignment.ALIGN_OPPOSITE]相反
     * [Layout.Alignment.ALIGN_CENTER]居中
     */
    var align: Layout.Alignment?=null

    /**
     * 添加图片
     * 缺点：图片的大小无法控制
     */
    var bitmap: Bitmap? = null
    var drawable: Drawable? = null
    var uri: Uri? = null
    @DrawableRes
    var resourceId:Int?=null

    /**
     * 超连接(网址，电话)
     * 类型一：网址，应该是以http如：http://www.baidu.com
     * 类型二：电话，应该是以tel开头：tel:13800138000
     * 类型三：邮件，应该是以mailto开头：mailto:495303648@qq.com
     * 类型四：短信，应该是以sms开头：sms:13800138000
     * 类型五：地图，应该是以geo开头：geo:29.542356,106.568154 效果：会打开地图并在地图上显示一个商家位置
     *
     * 注意要使其生效，需要如下使用,要传入true
     * textView.buildSpannableString (true){
     *      addText("去百度"){
     *          linkUrl = “http://www.baidu.com”
     *      }
     * }
     */
    var linkUrl: String? = null

    /**
     * 模糊字体:调用如下方法设置
     * @see setBlur
     */
    var blurFilter: BlurMaskFilter?=null

    /**
     * 设置圆角背景
     * @param color Int
     * @param txtColor Int
     * @param radius Int
     */
    fun setRadiusBgColor(@ColorInt color: Int, @ColorInt txtColor: Int, radius: Int) {
        radiusBgColorSpan = RadiusBackgroundSpan(color,txtColor,radius)
    }
    fun setQuoteColor(@ColorInt color: Int) {
        quoteSpan = QuoteSpan(color)
    }
    /**
     * 设置引用线条
     * @param color Int        线条颜色
     * @param stripeWidth Int  线条宽度  （Android9及以上有效）
     * @param gapWidth Int     间隙宽度：线条和文本之间的间隙（Android9及以上有效）
     */
    fun setQuoteColor(@ColorInt color: Int, stripeWidth:Int, gapWidth:Int) {
        quoteSpan = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            QuoteSpan(color,stripeWidth,gapWidth)
        }else{
            QuoteSpan(color)
        }
    }

    /**
     * 点击事件
     * 注意要使其生效，需要如下使用,要传入true
     * textView.buildSpannableString (true){
     *      addText("点击我"){
     *          onClick {  }
     *      }
     * }
     */
    fun onClick( onClick: (View) -> Unit) {
        onClickSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                //避免点击后：文字背景色高亮显示
                (widget as TextView).highlightColor = Color.TRANSPARENT
                onClick(widget)
            }
        }
    }

    /**
     * 设置缩进:写文章有用
     * @param first 首行缩进
     * @param rest  剩余行缩进
     */
    fun setLeadingMargin(first: Int, rest: Int){
        leadingMarginSpan = LeadingMarginSpan.Standard(first, rest)
    }

    /**
     * 设置列表标记
     * @param gapWidth        列表标记和文字间距离
     * @param color           列表标记的颜色
     * @param bulletRadius    列表标记的圆角（Android9及以上有效）
     * 注意：如果文本包含换行符，标记无效
     * 如果拼接的文本包含换行符，标记无效
     * 效果：文字的前面加个圆
     */
    fun setBullet(gapWidth: Int, color: Int, bulletRadius:Int) {
        bulletSpan = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            BulletSpan(gapWidth,color,bulletRadius)
        }else{
            BulletSpan(gapWidth,color)
        }

    }

    /**
     * 设置模糊
     * 尚存bug，其他地方存在相同的字体的话，相同字体出现在之前的话那么就不会模糊，出现在之后的话那会一起模糊
     * 推荐还是把所有字体都模糊这样使用
     * @param radius 模糊半径（需大于0）
     * @param style  模糊样式4种
     */
    fun setBlur(radius: Float, style: BlurMaskFilter.Blur?) {
        blurFilter = BlurMaskFilter(radius,style)
    }

}

/**
 * 背景带圆角，可设置颜色，角度
 * @param mColor     背景颜色
 * @param mTxtColor  文字颜色
 * @param mRadius    圆角半径
 */
class RadiusBackgroundSpan(@param:ColorInt private val mColor: Int, @param:ColorInt private val mTxtColor: Int, private val mRadius: Int) : ReplacementSpan() {
    private var mSize = 0
    override fun getSize(paint: Paint, text: CharSequence, start: Int, end: Int, fm: Paint.FontMetricsInt?): Int {
        mSize = (paint.measureText(text, start, end) + 2 * mRadius).toInt()
        //mSize就是span的宽度，span有多宽，开发者可以在这里随便定义规则
        //我的规则：这里text传入的是SpannableString，start，end对应setSpan方法相关参数
        //可以根据传入起始截至位置获得截取文字的宽度，最后加上左右两个圆角的半径得到span宽度
        return mSize
    }

    override fun draw(
        canvas: Canvas,
        text: CharSequence,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint
    ) {
        paint.color = mColor //设置背景颜色
        paint.isAntiAlias = true // 设置画笔的锯齿效果
        val oval = RectF(x, y + paint.ascent(), x + mSize, y + paint.descent())
        //设置文字背景矩形，x为span其实左上角相对整个TextView的x值，y为span左上角相对整个View的y值。paint.ascent()获得文字上边缘，paint.descent()获得文字下边缘
        canvas.drawRoundRect(oval,mRadius.toFloat(),mRadius.toFloat(), paint) //绘制圆角矩形，第二个参数是x半径，第三个参数是y半径
        paint.color = mTxtColor //恢复画笔的文字颜色
        canvas.drawText(text, start, end, x + mRadius, y.toFloat(), paint) //绘制文字
    }

}

