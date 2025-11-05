package com.outsbook.libs.canvaseditor.stickers

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toDrawable
import pl.droidsonroids.gif.GifDrawable

class GifSticker(
    private val context: Context,
    private val gifDrawable: GifDrawable,
    override val x: Float = 0f,
    override val y: Float = 0f
) : Sticker() {
    private val realBounds: Rect

    private var currentFrame = 0

    override var drawable: Drawable = getDrawableByFrame(9)

    final override val width: Int
        get() = drawable.intrinsicWidth

    final override val height: Int
        get() = drawable.intrinsicHeight

    init {
        realBounds = Rect(0, 0, width, height)
    }

    override fun setDrawable(drawable: Drawable): GifSticker {
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

    override fun setAlpha(alpha: Int): GifSticker {
        drawable.alpha = alpha
        return this
    }

    fun updateFrame() {
        currentFrame++
        val drawable = getDrawableByFrame(currentFrame % totalFrame())
        setDrawable(drawable)
    }

    private fun getDrawableByFrame(frame: Int): Drawable {
        return gifDrawable.seekToFrameAndGet(frame).toDrawable(context.resources)
    }

    fun totalFrame(): Int {
        return gifDrawable.numberOfFrames
    }

}