package com.outsbook.libs.canvaseditor.models.undoredo

import android.graphics.drawable.Drawable
import com.outsbook.libs.canvaseditor.constants.ActionMode
import com.outsbook.libs.canvaseditor.stickers.DrawableSticker
import com.outsbook.libs.canvaseditor.stickers.StickerView

internal class ReplaceImageCommand(
    private val drawable: Drawable,
    private val sticker: DrawableSticker,
    private val stickerView: StickerView
) : Command {

    private val preDrawable: Drawable = sticker.drawable

    override fun execute() {
        sticker.setDrawable(drawable)
        stickerView.updateMode(ActionMode.SELECT)
        stickerView.invalidate()
    }

    override fun undo() {
        sticker.setDrawable(preDrawable)
        stickerView.updateMode(ActionMode.NONE)
        stickerView.invalidate()
    }

    override fun redo() {
        sticker.setDrawable(drawable)
        stickerView.updateMode(ActionMode.NONE)
        stickerView.invalidate()
    }
}