package com.example.baozi_list.util

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Shader
import android.util.TypedValue
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest
import kotlin.math.min


/**
 * 2022/01/18 14:52.
 *
 *
 * Glide圆形
 */
class CircleCropTransform(unit: Int, borderSize: Float, borderColor: Int) :
    BitmapTransformation() {
    private val mBorderPaint: Paint?
    private val mBorderSize: Float

    /**
     * @param borderSize 边框宽度(px)
     * @param borderColor 边框颜色
     */
    @JvmOverloads
    constructor(
        borderSize: Float = 0f,
        borderColor: Int = Color.TRANSPARENT
    ) : this(TypedValue.COMPLEX_UNIT_DIP, borderSize, borderColor)

    /**
     * @param unit        borderSize 单位
     * @param borderSize 边框宽度(px)
     * @param borderColor 边框颜色
     */
    init {
        val displayMetrics = Resources.getSystem().displayMetrics
        mBorderSize = TypedValue.applyDimension(unit, borderSize, displayMetrics)
        mBorderPaint = Paint()
        mBorderPaint.isDither = true
        mBorderPaint.isAntiAlias = true
        mBorderPaint.color = borderColor
        mBorderPaint.style = Paint.Style.STROKE
        mBorderPaint.strokeWidth = mBorderSize
    }

    private fun circleCrop(pool: BitmapPool, source: Bitmap?): Bitmap? {
        if (source == null) return null

        val size =
            (min(source.width.toDouble(), source.height.toDouble()) - (mBorderSize / 2)).toInt()
        val x = (source.width - size) / 2
        val y = (source.height - size) / 2

        val squared = Bitmap.createBitmap(source, x, y, size, size)
        val result = pool[size, size, Bitmap.Config.ARGB_8888]
        val canvas = Canvas(result)
        val paint = Paint()
        paint.setShader(
            BitmapShader(
                squared,
                Shader.TileMode.CLAMP,
                Shader.TileMode.CLAMP
            )
        )
        paint.isAntiAlias = true
        val r = size / 2f
        canvas.drawCircle(r, r, r, paint)
        if (mBorderPaint != null) {
            val borderRadius = r - mBorderSize / 2
            canvas.drawCircle(r, r, borderRadius, mBorderPaint)
        }
        return result
    }

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        return circleCrop(pool, toTransform)!!
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
    }
}

