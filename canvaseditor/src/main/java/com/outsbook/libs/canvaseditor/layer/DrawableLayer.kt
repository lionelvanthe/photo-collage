package com.outsbook.libs.canvaseditor.layer

import android.graphics.Canvas
import com.outsbook.libs.canvaseditor.stickers.PictureSticker
import com.outsbook.libs.canvaseditor.stickers.Sticker

class DrawableLayer: Layer {
    private val stickers: MutableList<Sticker> = mutableListOf()

    override fun draw(canvas: Canvas) {
        stickers.forEach {
            it.draw(canvas)
        }
    }

    override fun findSelectSticker(x: Float, y: Float): Sticker? {
        return stickers.findLast { it.contains(x, y) && it !is PictureSticker }
    }

    fun addStickers(stickers: List<Sticker>) {
        this.stickers.addAll(stickers)
    }

    override fun addSticker(sticker: Sticker) {
        stickers.add(sticker)
    }

    override fun removeSticker(sticker: Sticker): Boolean {
        return stickers.remove(sticker)
    }

    fun clear() {
        stickers.clear()

    }
}


