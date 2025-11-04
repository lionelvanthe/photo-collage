package com.outsbook.libs.canvaseditor.models.undoredo

import com.outsbook.libs.canvaseditor.stickers.Sticker
import com.outsbook.libs.canvaseditor.stickers.StickerView

internal class AddStickerCommand(
    private val sticker: Sticker,
    private val stickerView: StickerView
) : Command {

    override fun execute() {
        stickerView.addStickerToView(sticker)
    }

    override fun undo() {
        stickerView.removeSticker(sticker)
    }

    override fun redo() {
        execute()
    }
}