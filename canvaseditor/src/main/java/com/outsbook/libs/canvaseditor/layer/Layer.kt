package com.outsbook.libs.canvaseditor.layer

import android.graphics.Canvas
import com.outsbook.libs.canvaseditor.stickers.Sticker

interface Layer {
    fun draw(canvas: Canvas)
    fun findSelectSticker(x: Float, y: Float): Sticker?
    fun removeSticker(sticker: Sticker): Boolean
    fun addSticker(sticker: Sticker)
}