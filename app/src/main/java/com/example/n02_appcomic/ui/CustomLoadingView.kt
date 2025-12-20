package com.example.n02_appcomic.ui

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.SweepGradient
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.graphics.toColorInt
import androidx.core.graphics.withRotation
import com.example.n02_appcomic.R

import kotlin.apply
import kotlin.math.min

class CustomLoadingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.BUTT
    }

    private val fgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.BUTT
    }

    private val rect = RectF()
    private var strokeWidth = 20f
    private var indeterminateDuration = 2000L

    private var rotationAngle = 0f
    private var animator: ValueAnimator? = null

    private var radarAnimator: ValueAnimator? = null

    private val sweepAngle = 360f

    private var gradient: SweepGradient? = null

    init {
        context.theme.obtainStyledAttributes(
            attrs, R.styleable.CustomLoadingView, 0, 0
        ).apply {
            try {
                strokeWidth = getDimension(R.styleable.CustomLoadingView_strokeWidth, 20f)
                indeterminateDuration =
                    getInt(R.styleable.CustomLoadingView_indeterminateDuration, 2000).toLong()
            } finally {
                recycle()
            }
        }

        bgPaint.strokeWidth = strokeWidth
        bgPaint.color = "#05E0DBFF".toColorInt()

        fgPaint.strokeWidth = strokeWidth
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startRotation()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopRotation()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        drawBackground(canvas = canvas)

        drawProgress(canvas = canvas)
    }

    private fun drawBackground(canvas: Canvas) {
        val size = min(width, height).toFloat()
        val radius = size / 2 - strokeWidth / 2
        rect.set(
            width / 2 - radius, height / 2 - radius, width / 2 + radius, height / 2 + radius
        )

        canvas.drawArc(rect, 0f, 360f, false, bgPaint)
    }

    private fun drawProgress(canvas: Canvas) {
        if (gradient == null) {
            gradient = SweepGradient(
                width / 2f, height / 2f, intArrayOf(
                    Color.TRANSPARENT,
                    "#10B981".toColorInt()
                ), floatArrayOf(0f, 1f)
            )
        }

        val matrix = Matrix()
        matrix.postRotate(-20f, width / 2f, height / 2f)
        gradient?.setLocalMatrix(matrix)
        fgPaint.shader = gradient

        canvas.withRotation(rotationAngle, width / 2f, height / 2f) {
            drawArc(rect, 0f, sweepAngle, false, fgPaint)
        }
    }

    fun startScanning() {
        startRotation()
    }

    fun stopScanning() {
        stopRotation()
    }

    private fun startRotation() {
        if (animator?.isRunning == true) return
        animator = ValueAnimator.ofFloat(0f, 360f).apply {
            duration = indeterminateDuration
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            addUpdateListener {
                rotationAngle = it.animatedValue as Float
                invalidate()
            }
            start()
        }
    }

    private fun stopRotation() {
        animator?.cancel()
        animator = null

        radarAnimator?.cancel()
        radarAnimator = null
    }
}