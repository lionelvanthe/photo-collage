package com.outsbook.libs.canvaseditor.models.undoredo

import android.graphics.Matrix
import com.outsbook.libs.canvaseditor.stickers.Sticker
import com.outsbook.libs.canvaseditor.stickers.StickerView


internal class ZoomAndRotateStickerCommand(
    private val fromMatrix: Matrix,
    private val toMatrix: Matrix,
    private val sticker: Sticker,
    private val stickerView: StickerView
) : Command {


    override fun execute() {
    }

    override fun undo() {
        sticker.setMatrix(fromMatrix)
        stickerView.invalidate()
    }

    override fun redo() {
        sticker.setMatrix(toMatrix)
        stickerView.invalidate()
    }
}