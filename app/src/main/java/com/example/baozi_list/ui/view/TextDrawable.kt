package com.example.baozi_list.ui.view

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable

class TextDrawable(private val text: String) : Drawable() {
    private val paint = Paint().apply {
        color = Color.BLACK
        textSize = 50f
        textAlign = Paint.Align.CENTER
    }

    override fun draw(canvas: Canvas) {
        canvas.drawText(text, bounds.exactCenterX(), bounds.exactCenterY() - (paint.descent() + paint.ascent()) / 2, paint)
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }
}
