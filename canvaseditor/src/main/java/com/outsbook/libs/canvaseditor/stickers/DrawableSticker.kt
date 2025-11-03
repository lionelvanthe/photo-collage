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
    private val defaultDrawable: Drawable,
    override val x: Float = 0f,
    override val y: Float = 0f,
    override val angle: Float = 0f
): Sticker() {
    protected val realBounds: Rect
        get() = Rect(0, 0, width, height)
    private var customDrawable: Drawable? = null
    override val drawable: Drawable
        get() = customDrawable?: defaultDrawable

    var mask: Bitmap? = null
    var drawablePus: Drawable? = null

    final override val width: Int
        get() = drawable.intrinsicWidth

    final override val height: Int
        get() = drawable.intrinsicHeight

    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)

    override fun setDrawable(drawable: Drawable): DrawableSticker {
        this.customDrawable = drawable
        matrix.reset()

        val targetWidth = defaultDrawable.intrinsicWidth.toFloat()
        val targetHeight = defaultDrawable.intrinsicHeight.toFloat()

        val srcWidth = drawable.intrinsicWidth.toFloat()
        val srcHeight = drawable.intrinsicHeight.toFloat()

        val scale = targetWidth / drawable.intrinsicWidth.toFloat()
        val dx = (targetWidth - srcWidth * scale) / 2f
        val dy = (targetHeight - srcHeight * scale) / 2f

        matrix.postScale(scale, scale)
        matrix.postTranslate(dx + x, dy + y)
        val centerX = x + targetWidth / 2f
        val centerY = y + targetHeight / 2f
        matrix.postRotate(angle, centerX, centerY)
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
        if (isDefault()) {
            drawablePus?.let { mid ->
                val centerX = x + width / 2f
                val centerY = y + height / 2f

                val midWidth = mid.intrinsicWidth
                val midHeight = mid.intrinsicHeight

                val left = (centerX - midWidth / 2).toInt()
                val top = (centerY - midHeight / 2).toInt()
                val right = (centerX + midWidth / 2).toInt()
                val bottom = (centerY + midHeight / 2).toInt()

                mid.setBounds(left, top, right, bottom)
                mid.draw(canvas)
            }
        }

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
            addRect(0f, 0f, defaultDrawable.intrinsicWidth.toFloat(), defaultDrawable.intrinsicHeight.toFloat(), Path.Direction.CW)
            transform(matrixToClip)
        }
        canvas.clipPath(clipPath)

    }

    fun isDefault(): Boolean {
        return customDrawable == null
    }

    override fun setAlpha(alpha: Int): DrawableSticker {
        drawable.alpha = alpha
        return this
    }
}