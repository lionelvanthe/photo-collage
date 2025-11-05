package com.outsbook.libs.canvaseditor.undoredo

import com.outsbook.libs.canvaseditor.undoredo.models.Command

internal open class CommandManager<T>(
    private val target: T
) {

    private val undoStack = ArrayDeque<Command<T>>()
    private val redoStack = ArrayDeque<Command<T>>()

    fun execute(command: Command<T>) {
        command.execute(target)
        undoStack.addLast(command)
        redoStack.clear()
    }

    fun undo() {
        if (undoStack.isNotEmpty()) {
            val cmd = undoStack.removeLast()
            cmd.undo(target)
            redoStack.addLast(cmd)
        }
    }

    fun redo() {
        if (redoStack.isNotEmpty()) {
            val cmd = redoStack.removeLast()
            cmd.redo(target)
            undoStack.addLast(cmd)
        }
    }

    fun canUndo(): Boolean {
        return undoStack.isNotEmpty()
    }

    fun canRedo(): Boolean {
        return redoStack.isNotEmpty()
    }
}