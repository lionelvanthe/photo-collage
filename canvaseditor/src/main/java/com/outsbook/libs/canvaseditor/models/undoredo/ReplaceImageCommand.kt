package com.outsbook.libs.canvaseditor.models.undoredo

import android.graphics.Matrix
import android.graphics.drawable.Drawable
import com.outsbook.libs.canvaseditor.stickers.DrawableSticker
import com.outsbook.libs.canvaseditor.stickers.StickerView

internal class ReplaceImageCommand(
    private val preDrawable: Drawable,
    private val drawable: Drawable,
    private val matrix: Matrix? = null,
    private val sticker: DrawableSticker,
    private val stickerView: StickerView
) : Command {

    override fun execute() {
        stickerView.updateDrawableOfSticker(drawable, sticker, null)
    }

    override fun undo() {
        stickerView.updateDrawableOfSticker(preDrawable, sticker, matrix)
    }

    override fun redo() {
        execute()
    }
}