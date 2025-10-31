package app.aicalories.foodscan.photocollage

import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Environment
import android.view.MotionEvent
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColorInt
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import app.aicalories.foodscan.photocollage.databinding.ActivityMainBinding
import com.airbnb.lottie.LottieCompositionFactory
import com.airbnb.lottie.LottieDrawable
import com.google.gson.Gson
import com.outsbook.libs.canvaseditor.listeners.CanvasEditorListener
import com.outsbook.libs.canvaseditor.stickers.DrawableSticker
import com.welly.myapplication.lottierecorder.FrameCreator
import com.welly.myapplication.lottierecorder.Recorder
import com.welly.myapplication.lottierecorder.RecordingOperation
import java.io.File


class MainActivity : AppCompatActivity() {

    private var strokeWidth: Float = 20f

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initValue()
        initClickListener()
        initCanvasEditorListener()
        getDiyConfig()

/*        // 1️⃣ Load composition từ assets
        val inputStream = this@MainActivity.assets.open("animation.json")
        val result = LottieCompositionFactory.fromJsonInputStreamSync(inputStream, "animation.json")
        val composition = result.value ?: throw Exception("Lottie parse failed: ${result.exception}")
        inputStream.close()
        // 2️⃣ Tạo drawable render animation
        val lottieDrawable = LottieDrawable().apply {
            setComposition(composition)
            setImageAssetDelegate { asset ->
                try {
                    val input = this@MainActivity.assets.open("images/${asset.fileName}")
                    BitmapFactory.decodeStream(input)
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }
            }
        }

        val path = getExternalFilesDir(Environment.DIRECTORY_PICTURES) ?: File(
            cacheDir,
            Environment.DIRECTORY_PICTURES
        ).apply { mkdirs() }
        val videoFile = File(path, "lottie_in_video.mp4")
        val recordingOperation =
            RecordingOperation(Recorder(videoOutput = videoFile), FrameCreator(lottieDrawable))
            {
                //ToDO: convert done
            }

        recordingOperation.start()  // Make sure to call this on a background thread!*/


//        // 1️⃣ Khai báo thư mục chứa ảnh (phải nằm trong assets/)
//        binding.lottieAnimationView.setImageAssetsFolder("images")
//
//// 2️⃣ Rồi mới load animation
//        binding.lottieAnimationView.setAnimation("animation.json")
//
//// 3️⃣ (Tuỳ chọn) Bắt đầu phát
//        binding.lottieAnimationView.playAnimation()


    }

    private fun initValue(){
        binding.buttonUndo.imageAlpha = 50
        binding.buttonRedo.imageAlpha = 50
        //set stroke width
        binding.canvasEditor.setStrokeWidth(strokeWidth)
        //set paint color
        binding.canvasEditor.setPaintColor(ContextCompat.getColor(this, R.color.colorBlack))
    }

    private fun initClickListener(){
        binding.buttonSticker.setOnClickListener{
            //Add drawable sticker
            val drawable = ContextCompat.getDrawable(this, R.drawable.app_icon)
            drawable?.let {
                binding.canvasEditor.addDrawableSticker(it)
            }
        }

        binding.ivDonePaint.setOnClickListener {
            binding.canvasEditor.donePaint()
            binding.viewBrush.visibility = View.GONE
        }

        binding.buttonText.setOnClickListener{
            //Add text sticker
            val text = "Canvas"
            val textColor = ContextCompat.getColor(this, R.color.colorPrimary)
            binding.canvasEditor.addTextSticker(text, textColor, null)
        }

        binding.buttonStickerText.setOnClickListener{
            //Add text with drawable sticker
            val drawable = ContextCompat.getDrawable(this, R.drawable.ic_panorama_240dp)
            val text = "Canvas"
            val textColor = ContextCompat.getColor(this, R.color.colorAccent)
            drawable?.let{
                binding.canvasEditor.addDrawableTextSticker(it, text, textColor, null)
            }
        }

        binding.buttonBlack.setOnClickListener {
            binding.buttonPlus.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_plus_black_24dp))
            binding.buttonMinus.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_minus_black_24dp))
            val color = ContextCompat.getColor(this, R.color.colorBlack)
            binding.canvasEditor.setPaintColor(color)
            binding.canvasEditor.enablePaintView()
            binding.viewBrush.visibility = View.VISIBLE
        }

        binding.buttonYellow.setOnClickListener {
            binding.buttonPlus.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_plus_yellow_24dp))
            binding.buttonMinus.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_minus_yellow_24dp))
            val color = ContextCompat.getColor(this, R.color.colorYellow)
            binding.canvasEditor.setPaintColor(color)
            binding.canvasEditor.enableFollowTextView()
            binding.viewBrush.visibility = View.VISIBLE
        }

        binding.buttonPlus.setOnClickListener {
            strokeWidth += 10f
            binding.canvasEditor.setStrokeWidth(strokeWidth)
            binding.canvasEditor.doneTextBrush()
            binding.viewBrush.visibility = View.GONE

        }

        binding.buttonMinus.setOnClickListener {
            strokeWidth -= 10f
            binding.canvasEditor.setStrokeWidth(strokeWidth)
        }

        binding.buttonSave.setOnClickListener {
            val bitmap = binding.canvasEditor.downloadBitmap()
            binding.imageView.setImageBitmap(bitmap)
            binding.viewImagePreview.visibility = View.VISIBLE
        }

        binding.buttonUndo.setOnClickListener {
            binding.canvasEditor.undo()
        }

        binding.buttonDelete.setOnClickListener {
            binding.canvasEditor.removeAll()
        }

        binding.buttonRedo.setOnClickListener {
            binding.canvasEditor.redo()
        }

        binding.buttonClose.setOnClickListener {
            binding.viewImagePreview.visibility = View.GONE
        }
    }

    private fun initCanvasEditorListener(){
        binding.canvasEditor.setListener(object: CanvasEditorListener {
            override fun onEnableUndo(isEnable: Boolean) {
                // isEnable = true (undo list is not empty)
                // isEnable = false (undo list is empty)
                binding.buttonUndo.imageAlpha = if(isEnable) 255 else 50
            }

            override fun onEnableRedo(isEnable: Boolean) {
                // isEnable = true (redo list is not empty)
                // isEnable = false (redo list is empty)
                binding.buttonRedo.imageAlpha = if(isEnable) 255 else 50
            }

            override fun onTouchEvent(event: MotionEvent) {
                //When the canvas touch
            }

            override fun onStickerActive() {
                //When a sticker change to active mode
            }

            override fun onStickerRemove() {
                //When a sticker remove from canvas
            }

            override fun onStickerDone() {
                //When the active sticker added to canvas
            }

            override fun onStickerZoomAndRotate() {
                //When the active sticker zoom or rotate
            }

            override fun onStickerFlip() {
                //When the active sticker flip
            }
        })
    }

    private fun getDiyConfig() {
        binding.canvasEditor.post {
            val jsonString = assets.open("data.json").bufferedReader().use { it.readText() }
            val template = Gson().fromJson(jsonString, DiyConfig::class.java)

            val layoutParams = binding.canvasEditor.layoutParams as ConstraintLayout.LayoutParams
            layoutParams.dimensionRatio = "${template.width}:${template.height}"
            binding.canvasEditor.layoutParams = layoutParams

            binding.canvasEditor.setBackgroundColor(template.background.toColorInt())

            val ratio = binding.canvasEditor.height.toFloat() / template.height
            val stickers = template.elements.mapNotNull {
                if (it.type == "Image") {
                    val drawable = GradientDrawable()
                    drawable.shape = GradientDrawable.RECTANGLE
                    drawable.setColor(Color.GRAY)
                    val widthPx = (it.width*ratio).toInt()
                    val heightPx = (it.height*ratio).toInt()

                    drawable.setSize(widthPx, heightPx)
                    DrawableSticker(
                        drawable,
                        it.x.toFloat() * ratio,
                        it.y.toFloat() * ratio,
                        it.angle
                    )
                } else {
                    null
                }
            }
            binding.canvasEditor.addDrawableStickers(stickers)
        }
    }
}