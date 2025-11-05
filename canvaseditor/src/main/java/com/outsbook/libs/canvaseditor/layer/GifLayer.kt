package com.outsbook.libs.canvaseditor.layer

import android.graphics.Canvas
import com.outsbook.libs.canvaseditor.stickers.GifSticker
import com.outsbook.libs.canvaseditor.stickers.Sticker

class GifLayer : Layer {
    private var sticker: GifSticker? = null

    override fun draw(canvas: Canvas) {
        sticker?.draw(canvas)
    }

    override fun findSelectSticker(x: Float, y: Float): Sticker? {
        return sticker?.takeIf { it.contains(x, y) }
    }

    override fun addSticker(sticker: Sticker) {
        this.sticker = sticker as GifSticker
    }

    override fun removeSticker(sticker: Sticker): Boolean {
        val success = this.sticker == sticker
        clear()
        return success
    }

    fun updateFrameGif() {
        sticker?.updateFrame()
    }

    fun getTotalFrameOfGif(): Int {
        return sticker?.totalFrame() ?: 1
    }

    fun clear() {
        this.sticker = null
    }
}


