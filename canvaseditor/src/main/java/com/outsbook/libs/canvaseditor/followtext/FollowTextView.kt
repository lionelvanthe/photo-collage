package com.outsbook.libs.canvaseditor.followtext

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.atan2
import kotlin.math.hypot
import kotlin.math.max


class FollowTextView : View {
    private var text: String = "HELLO"
    private val textPaint: Paint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG).apply {
            textAlign = Paint.Align.CENTER
            textSize = textSizePx
            style = Paint.Style.FILL
        }
    }

    private var textSizePx = 64f
    private var spacing = 60f // khoảng cách giữa các ký tự trên path

    private val allPaths: MutableList<MutableList<PointF>> = ArrayList()
    private var currentRaw: MutableList<PointF>? = null


    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
    }

    // public API
    fun setText(s: String?) {
        var s = s
        if (s == null) s = ""
        this.text = s
        invalidate()
    }

    fun setTextSizePx(px: Float) {
        textSizePx = px
        textPaint.textSize = px
        invalidate()
    }

    fun setSpacing(px: Float) {
        spacing = max(1f, px)
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                // 🔹 Tạo path mới
                currentRaw = ArrayList()
                currentRaw?.add(PointF(x, y))
                allPaths.add(currentRaw!!)
                invalidate()
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                var i = 0
                while (i < event.historySize) {
                    val hx = event.getHistoricalX(i)
                    val hy = event.getHistoricalY(i)
                    currentRaw?.add(PointF(hx, hy))
                    i++
                }
                currentRaw?.add(PointF(x, y))
                invalidate()
                return true
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                currentRaw = null
                invalidate()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawText(canvas)
    }

    // tái sử dụng các hàm cũ:
    private fun resamplePath(rawPoints: MutableList<PointF>): MutableList<PointF> {
        val sampled: MutableList<PointF> = ArrayList<PointF>()
        if (rawPoints.isEmpty()) return sampled

        sampled.add(PointF(rawPoints[0].x, rawPoints[0].y))
        var accum = 0f
        for (i in 1..<rawPoints.size) {
            val p0 = rawPoints[i - 1]
            val p1 = rawPoints[i]
            val dx = p1.x - p0.x
            val dy = p1.y - p0.y
            val segLen = hypot(dx.toDouble(), dy.toDouble()).toFloat()
            if (segLen == 0f) continue

            var t = 0f
            while (accum + (segLen * (1 - t)) >= spacing) {
                val need = spacing - accum
                val ratio = need / segLen
                val nx = p0.x + (dx * (t + ratio))
                val ny = p0.y + (dy * (t + ratio))
                sampled.add(PointF(nx, ny))
                t += ratio
                accum = 0f
            }
            accum += segLen * (1 - t)
        }
        return sampled
    }

    private fun drawTextAlongPoints(canvas: Canvas, sampled: MutableList<PointF>) {
        val nSamples = sampled.size
        val nChars = text.length
        if (nSamples == 0) return

        for (i in 0..<nSamples) {
            val p = sampled[i]
            val charIndex = i % nChars
            val c = text[charIndex]

            var angleDeg = 0f
            if (i + 1 < nSamples) {
                val next = sampled[i + 1]
                angleDeg =
                    Math.toDegrees(atan2((next.y - p.y).toDouble(), (next.x - p.x).toDouble()))
                        .toFloat()
            } else if (i - 1 >= 0) {
                val prev = sampled[i - 1]
                angleDeg =
                    Math.toDegrees(atan2((p.y - prev.y).toDouble(), (p.x - prev.x).toDouble()))
                        .toFloat()
            }

            canvas.save()
            canvas.translate(p.x, p.y)
            canvas.rotate(angleDeg)

            val fm = textPaint.fontMetrics
            val centerY = -((fm?.ascent?:0f) + (fm?.descent?:0f)) / 2f
            canvas.drawText(c.toString(), 0f, centerY, textPaint)
            canvas.restore()
        }
    }

    fun computeBoundsForAllPaths(): RectF {
        val totalBounds = RectF()
        var first = true

        for (rawPoints in allPaths) {
            val sampled: MutableList<PointF> = resamplePath(rawPoints)
            val pathBounds: RectF = computeBoundsForPath(sampled)

            if (pathBounds.isEmpty) continue

            if (first) {
                totalBounds.set(pathBounds)
                first = false
            } else {
                totalBounds.union(pathBounds)
            }
        }

        return totalBounds
    }

    fun drawText(canvas: Canvas) {
        if (allPaths.isEmpty() || text.isEmpty()) return

        for (rawPoints in allPaths) {
            val sampled = resamplePath(rawPoints)
            drawTextAlongPoints(canvas, sampled)
        }
    }

    private fun computeBoundsForPath(sampled: MutableList<PointF>): RectF {
        val bounds = RectF()
        var first = true
        val fm = textPaint.getFontMetrics()
        val textHeight = fm.descent - fm.ascent

        for (i in sampled.indices) {
            val p = sampled[i]
            val c = text[i % text.length]
            val charWidth = textPaint.measureText(c.toString())

            val left = p.x - charWidth / 2f
            val top = p.y - textHeight / 2f
            val right = p.x + charWidth / 2f
            val bottom = p.y + textHeight / 2f

            if (first) {
                bounds.set(left, top, right, bottom)
                first = false
            } else {
                bounds.union(left, top, right, bottom)
            }
        }
        return bounds
    }

    fun clear() {
        allPaths.clear()
        currentRaw?.clear()
        currentRaw = null
    }
}