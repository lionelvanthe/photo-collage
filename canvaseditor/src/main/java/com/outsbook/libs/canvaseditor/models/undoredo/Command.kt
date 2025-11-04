package com.outsbook.libs.canvaseditor.models.undoredo

interface Command {
    fun execute()
    fun undo()
    fun redo()
}