package app.aicalories.foodscan.photocollage

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import app.aicalories.foodscan.photocollage.databinding.ActivityMainBinding
import com.outsbook.libs.canvaseditor.listeners.CanvasEditorListener

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
        }

        binding.buttonYellow.setOnClickListener {
            binding.buttonPlus.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_plus_yellow_24dp))
            binding.buttonMinus.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_minus_yellow_24dp))
            val color = ContextCompat.getColor(this, R.color.colorYellow)
            binding.canvasEditor.setPaintColor(color)
        }

        binding.buttonPlus.setOnClickListener {
            strokeWidth += 10f
            binding.canvasEditor.setStrokeWidth(strokeWidth)
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
}