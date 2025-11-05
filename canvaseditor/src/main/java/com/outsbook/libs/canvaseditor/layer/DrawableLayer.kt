package com.outsbook.libs.canvaseditor.layer

import android.graphics.Canvas
import com.outsbook.libs.canvaseditor.stickers.GifSticker
import com.outsbook.libs.canvaseditor.stickers.PictureSticker
import com.outsbook.libs.canvaseditor.stickers.Sticker

class DrawableLayer {
    private val stickers: MutableList<Sticker> = mutableListOf()

    fun draw(canvas: Canvas) {
        stickers.forEach {
            it.draw(canvas)
        }
    }

    fun findSelectSticker(x: Float, y: Float): Sticker? {
        return stickers.findLast { it.contains(x, y) && it !is PictureSticker }
    }

    fun addStickers(stickers: List<Sticker>) {
        this.stickers.addAll(stickers)
    }

    fun addSticker(sticker: Sticker) {
        stickers.add(sticker)
    }

    fun removeSticker(sticker: Sticker): Boolean {
        return stickers.remove(sticker)
    }

    fun isContainsGifSicker(): Boolean {
        return stickers.any { it is GifSticker }
    }

    fun getFirstSticker(): Sticker? {
        return stickers.firstOrNull()
    }

    fun updateFrameGif() {
        (getFirstSticker() as? GifSticker)?.updateFrame()

    }

    fun clear() {
        stickers.clear()

    }
}


