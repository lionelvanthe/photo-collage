package com.outsbook.libs.canvaseditor.stickers

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable

open class PictureDrawableSticker(
    override var drawable: Drawable,
    override val x: Float = 0f,
    override val y: Float = 0f,
    override val angle: Float = 0f
): DrawableSticker(drawable, x, y, angle) {

    override fun setDrawable(drawable: Drawable): PictureDrawableSticker {
        this.drawable = drawable
        return this
    }

    override fun draw(canvas: Canvas) {
        canvas.save()
        canvas.concat(matrix)
        drawable.bounds = realBounds
        drawable.draw(canvas)
        canvas.restore()
    }

    override fun setAlpha(alpha: Int): PictureDrawableSticker {
        drawable.alpha = alpha
        return this
    }
}