package com.outsbook.libs.canvaseditor.undoredo.models.sticker

import com.outsbook.libs.canvaseditor.stickers.Sticker
import com.outsbook.libs.canvaseditor.stickers.StickerView

internal class AddStickerCommand(
    private val sticker: Sticker,
) : StickerCommand {

    override fun execute(target: StickerView) {
        target.addStickerToView(sticker)
    }

    override fun undo(target: StickerView) {
        target.removeStickerInView(sticker)
    }

    override fun redo(target: StickerView) {
        execute(target)
    }
}