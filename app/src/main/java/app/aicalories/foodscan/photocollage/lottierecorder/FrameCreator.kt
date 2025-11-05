package app.aicalories.foodscan.photocollage.lottierecorder

import android.graphics.Canvas
import com.airbnb.lottie.LottieDrawable
import com.outsbook.libs.canvaseditor.layer.DrawableLayer
import com.outsbook.libs.canvaseditor.stickers.GifSticker

class FrameCreator(
    private val lottieDrawable: LottieDrawable?,
    private val layers: List<DrawableLayer>) {
    private val scale = lottieDrawable?.let {
        VIDEO_WIDTH_PX/it.intrinsicWidth
    }?: 1f
    init {
        lottieDrawable?.apply {
            setBounds(
            0, 0, (composition.bounds.width() * scale).toInt(),
            (composition.bounds.height() * scale).toInt())
        }
    }

    private fun getFrameOfGif(): Int {
        for (i in layers.size -1 downTo 0) {
            if (layers[i].isContainsGifSicker()) {
                return (layers[i].getFirstSticker() as? GifSticker)?.totalFrame()?: 1
            }
        }
        return 1
    }

    private val lottieFrames = lottieDrawable?.composition?.durationFrames?.toInt()?:1
    private val durationInFrames: Int = run {
        val gifFrames = getFrameOfGif()
        when {
            lottieFrames > gifFrames -> lottieFrames
            gifFrames > 100 -> gifFrames
            else -> 100
        }
    }

    private var currentFrame: Int = 0

    fun renderToCanvas(canvas: Canvas) {
        lottieDrawable?.let {
            lottieDrawable.frame = currentFrame% lottieFrames
            lottieDrawable.draw(canvas)
        }
        for (layer in layers) {
            layer.draw(canvas)
            layer.updateFrameGif()
        }
        currentFrame++
    }

    fun hasEnded() = currentFrame > durationInFrames
}

private const val VIDEO_WIDTH_PX = 1080f