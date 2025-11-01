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

open class DrawableSticker(
    override var drawable: Drawable,
    override val x: Float = 0f,
    override val y: Float = 0f,
    override val angle: Float = 0f
): Sticker() {
    protected val realBounds: Rect

    var mask: Bitmap? = null

    final override val width: Int
        get() = drawable.intrinsicWidth

    final override val height: Int
        get() = drawable.intrinsicHeight

    init {
        realBounds = Rect(0, 0, width, height)
    }

    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)

    override fun setDrawable(drawable: Drawable): DrawableSticker {
        this.drawable = drawable
        return this
    }

    override fun draw(canvas: Canvas) {
        val layerId = canvas.saveLayer(null, null)
        canvas.save()
        clip(canvas)
        canvas.concat(matrix)
        drawable.bounds = realBounds
        drawable.draw(canvas)
        canvas.restore()

        mask?.let {
            paint.xfermode = xfermode
            canvas.drawBitmap(it, null,
                RectF(x, y, x + it.width, y + it.height), paint)
            paint.xfermode = null
        }
        canvas.restoreToCount(layerId)
    }

    protected open fun clip(canvas: Canvas) {
        val clipPath = Path().apply {
            addRect(0f, 0f, width.toFloat(), height.toFloat(), Path.Direction.CW)
            transform(matrixToClip)
        }
        canvas.clipPath(clipPath)

    }

    override fun setAlpha(alpha: Int): DrawableSticker {
        drawable.alpha = alpha
        return this
    }
}