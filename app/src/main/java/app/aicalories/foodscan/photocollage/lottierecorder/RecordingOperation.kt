 package app.aicalories.foodscan.photocollage.lottierecorder

class RecordingOperation(
    private val recorder: Recorder,
    private val frameCreator: FrameCreator,
    private val listener: () -> Unit
) {

    fun start() {
        while (isRecording()) {
            recorder.nextFrame(frameCreator)
        }
        recorder.end()
        listener()
    }

    private fun isRecording() = !frameCreator.hasEnded()
}