package com.outsbook.libs.canvaseditor.undoredo.models

internal interface Command<T> {
    fun execute(target: T)
    fun undo(target: T)
    fun redo(target: T)
}