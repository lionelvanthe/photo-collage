package com.outsbook.libs.canvaseditor.undoredo.models.sticker

import android.graphics.Matrix
import android.graphics.drawable.Drawable
import com.outsbook.libs.canvaseditor.stickers.DrawableSticker
import com.outsbook.libs.canvaseditor.stickers.StickerView

internal class ReplaceImageCommand(
    private val preDrawable: Drawable,
    private val drawable: Drawable,
    private val matrix: Matrix? = null,
    private val sticker: DrawableSticker,
) : StickerCommand {

    override fun execute(target: StickerView) {
        target.updateDrawableOfSticker(drawable, sticker, null)
    }

    override fun undo(target: StickerView) {
        target.updateDrawableOfSticker(preDrawable, sticker, matrix)
    }

    override fun redo(target: StickerView) {
        execute(target)
    }
}