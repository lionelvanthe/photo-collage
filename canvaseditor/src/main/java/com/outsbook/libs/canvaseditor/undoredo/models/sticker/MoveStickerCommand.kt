package com.outsbook.libs.canvaseditor.undoredo.models.sticker

import android.graphics.Matrix
import com.outsbook.libs.canvaseditor.stickers.Sticker
import com.outsbook.libs.canvaseditor.stickers.StickerView


internal class MoveStickerCommand(
    private val fromMatrix: Matrix,
    private val toMatrix: Matrix,
    private val sticker: Sticker,
) : StickerCommand {

    override fun execute(target: StickerView) {
    }

    override fun undo(target: StickerView) {
        sticker.setMatrix(fromMatrix)
        target.invalidate()
    }

    override fun redo(target: StickerView) {
        sticker.setMatrix(toMatrix)
        target.invalidate()
    }
}