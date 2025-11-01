package com.outsbook.libs.canvaseditor.stickers

import android.graphics.Canvas
import android.graphics.Path
import android.graphics.Rect
import android.graphics.drawable.Drawable

open class PictureSticker(
    override var drawable: Drawable,
    override val x: Float = 0f,
    override val y: Float = 0f,
    override val angle: Float = 0f
): Sticker() {
    protected val realBounds: Rect

    final override val width: Int
        get() = drawable.intrinsicWidth

    final override val height: Int
        get() = drawable.intrinsicHeight

    init {
        realBounds = Rect(0, 0, width, height)
    }

    override fun setDrawable(drawable: Drawable): PictureSticker {
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

    override fun setAlpha(alpha: Int): PictureSticker {
        drawable.alpha = alpha
        return this
    }
}