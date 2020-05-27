package com.maliotis.iosalert

import android.animation.ValueAnimator
import android.app.Activity
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.*
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.maliotis.iosalert.blurview.BlurView
import com.maliotis.iosalert.blurview.RenderScriptBlur


/**
 * Created by petrosmaliotis on 26/05/2020.
 */

private val TAG = IOSAlert::class.java.simpleName
class IOSAlert private constructor(private val activity1: Activity,
                                   var title: String? = null,
                                   var body: String?,
                                   var typeface: Typeface?,
                                   var positiveText: String?,
                                   var negativeText: String?,
                                   var iosPositiveClickListener: IOSClickListener,
                                   var iosNegativeClickListener: IOSClickListener?): DialogFragment() {

    var blurRadius: Float = 25f
    var backgroundColor: Int = 0xDFFFFFFF.toInt()
    var tintButtons: Boolean = true
    var tintButtonsColor: Int = 0
    var cornerRadius: Float = 10f


    /**
     * Builder class to build a dialog
     */
    data class Builder (val activity: AppCompatActivity) {
        // variables
        var title: String? = null
        var body: String? = null
        var typeface: Typeface? = null
        var positiveText: String? = null
        var negativeText: String? = null
        var backgroundColor: Int = 0xDFFFFFFF.toInt()
        var isCancellable: Boolean = true
        var tintButtons: Boolean = true
        var tintButtonsColor: Int = 0x38D3D3D3
        var cornerRadius: Float = 10f
        var iosPositiveClickListener: IOSClickListener = object: IOSClickListener {}
        var iosNegativeClickListener: IOSClickListener? = null
        private var blurRadius: Float = 25f
        private var iosAlert: IOSAlert? = null
        val fragmentTag = "iOSAlert"

        // setters functions
        fun title(title: String) = apply { this.title = title }
        fun body(body: String) = apply { this.body  = body }
        fun typeface(typeface: Typeface) = apply { this.typeface = typeface }
        fun positiveText(positiveText: String) = apply { this.positiveText = positiveText }
        fun negativeText(negativeText: String) = apply { this.negativeText = negativeText }

        /**
         * Positive Click Listener - remember to call dismiss if you override
         */
        fun iOSAlertPositiveClickListener(IOSClickListener: IOSClickListener) = apply {
            this.iosPositiveClickListener = IOSClickListener
        }

        /**
         * Negative Click Listener - remember to call dismiss if you override
         */
        fun iOSAlertNegativeClickListener(IOSClickListener: IOSClickListener) = apply {
            iosNegativeClickListener = IOSClickListener
        }

        /**
         * Set transparency level with white background <br></br>  
         * 1 fully transparent
         */
        fun transparency(@FloatRange(from = 0.0, to = 1.0) alpha: Float) = apply {
            val alphaParam = ((1.0 - alpha) * 255).toInt()
            backgroundColor = Color.argb(alphaParam, 255, 255, 255)
        }

        /**
         * Convenient method to pass a different color
         */
        fun backgroundColor(@IntRange(from = 0, to = 255) alpha: Int,
                            @IntRange(from = 0, to = 255) red: Int,
                            @IntRange(from = 0, to = 255) green: Int,
                            @IntRange(from = 0, to = 255) blue: Int) = apply {
            backgroundColor = Color.argb(alpha, red, green, blue)
        }

        /**
         * Pass the Int color
         */
        fun backgroundColor(color: Int) = apply { backgroundColor = color }

        /**
         * Set tintButtons
         */
        fun tintButtons(tintButtons: Boolean) = apply { this.tintButtons = tintButtons }

        /**
         * Set tintButtonsColor
         */
        fun tintButtonsColor(tintButtonsColor: Int) = apply { this.tintButtonsColor = tintButtonsColor }

        /**
         * Sets the fragments isCancellable attribute to true or false
         * [DialogFragment.isCancelable]
         */
        fun isCancellable(isCancellable: Boolean) = apply { this.isCancellable = isCancellable }

        /**
         * @param radius provide a blur 0 < radius <= 25
         */
        fun blurRadius(@FloatRange(from=0.01, to=25.0) radius: Float) = apply {
            if (radius > 0f && radius <= 25f)
                blurRadius = radius
        }

        fun cornerRadius(cornerRadius: Float) = apply { this.cornerRadius = cornerRadius }

        /**
         * Will build and return the iOSAlert instance with the parameters specified in the Builder
         */
        fun build() : IOSAlert {
            iosAlert = IOSAlert(activity, title, body, typeface, positiveText, negativeText,iosPositiveClickListener, iosNegativeClickListener)
            iosAlert!!.blurRadius = blurRadius
            iosAlert!!.backgroundColor = backgroundColor
            iosAlert!!.isCancelable = isCancellable
            iosAlert!!.tintButtons = tintButtons
            iosAlert!!.tintButtonsColor = tintButtonsColor
            iosAlert!!.cornerRadius = cornerRadius
            return iosAlert!!
        }

        /**
         * Will build and show the iOSAlert instance with the parameters specified in the Builder
         */
        fun buildAndShow() {
            build()
            iosAlert!!.show(activity.supportFragmentManager, fragmentTag)
        }

    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val v = inflater.inflate(R.layout.popup_layout, container, false) as LinearLayout
        val linearLayout: LinearLayout =  v.findViewById(R.id.linear_layout)
        val blurView: BlurView = v.findViewById(R.id.blurView)
        val titleTextView: TextView = v.findViewById(R.id.popUpTitle)
        val bodyTextView: TextView = v.findViewById(R.id.popUpBody)
        val positiveButton: Button = v.findViewById(R.id.popUpOkButton)
        var negativeButton: Button? = null

        if (body.isNullOrEmpty()) blurView.findViewById<LinearLayout>(R.id.layoutInsideBlurView).removeView(bodyTextView)
        else bodyTextView.text = body

        title = title ?: ""
        titleTextView.text = title
        positiveText = positiveText ?: "OK"

        val positiveClickListener = getPositiveButtonClickListener()
        if (negativeText != null || iosNegativeClickListener != null) {
            // Add a negative button
            negativeButton = getNegativeButton()
            val lineSeperator = getLineSeparator()
            linearLayout.addView(lineSeperator, 0)
            linearLayout.addView(negativeButton, 0)

        }
        positiveButton.setOnClickListener(positiveClickListener)
        if (tintButtons)
            positiveButton.setOnTouchListener(getOnTouchTintListener())
        // set typeface
        setTypefaceSettings(titleTextView, bodyTextView, positiveButton, negativeButton)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Blur background

        val decorView: View = activity1.window.decorView
        val rootView = decorView.findViewById<View>(android.R.id.content) as ViewGroup
        val windowBackground = decorView.background


        val backgroundDrawable = GradientDrawable()
        backgroundDrawable.cornerRadius = dpToPixel(cornerRadius, resources)
        backgroundDrawable.setColor(backgroundColor)
        blurView.background = backgroundDrawable

        
        blurView.setupWith(rootView)
            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(RenderScriptBlur(activity1))
            .setBlurRadius(blurRadius)
            .setHasFixedTransformationMatrix(true)

        blurView.outlineProvider = ViewOutlineProvider.BACKGROUND
        blurView.clipToOutline = true

        return v
    }

    /**
     * Check if the view exists and if it is of type TextView or Subclass of that assign the typeface
     */
    private fun setTypefaceSettings(vararg views: View?) {
        for (v in views) {
            if (v != null) {
                if (v is TextView) {
                    v.typeface = typeface
                }
            }
        }
    }

    private fun getPositiveButtonClickListener(): View.OnClickListener {
        return View.OnClickListener {
            iosPositiveClickListener.onClick(dialog)
        }
    }

    private fun getNegativeButtonClickListener(): View.OnClickListener {
        return View.OnClickListener {
            if (iosNegativeClickListener == null)
                iosNegativeClickListener = object: IOSClickListener {}
            iosNegativeClickListener?.onClick(dialog)
        }
    }

    private fun getOnTouchTintListener(): View.OnTouchListener {
        return View.OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val background = ColorDrawable()
                val anim = ValueAnimator.ofArgb(Color.TRANSPARENT, tintButtonsColor)
                anim.addUpdateListener {
                    val value = it.animatedValue as Int
                    background.color = value
                    v.background = background

                }
                anim.duration = 150
                anim.start()

            } else if (event.action == MotionEvent.ACTION_UP) {

                val background = ColorDrawable()
                val anim = ValueAnimator.ofArgb(tintButtonsColor, Color.TRANSPARENT)
                anim.addUpdateListener {
                    val value = it.animatedValue as Int
                    background.color = value
                    v.background = background

                }
                anim.duration = 150
                anim.start()
            }

            false
        }
    }

    private fun getNegativeButton(): Button {
        return Button(activity1).apply {
            setTextColor(Color.RED)
            text = negativeText ?: "CANCEL"
            if (this@IOSAlert.typeface != null)
                typeface = this@IOSAlert.typeface

            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT, 0.5f)
            background = ColorDrawable(Color.TRANSPARENT)
            setOnClickListener(getNegativeButtonClickListener())
            if (tintButtons)
                setOnTouchListener(getOnTouchTintListener())
        }
    }

    private fun getLineSeparator(): View {
        return View(activity1).apply {
            layoutParams = LinearLayout.LayoutParams(dpToPixel(0.5f, resources).toInt(), MATCH_PARENT)
            setBackgroundColor(Color.parseColor("#9FAAAAAA"))

        }
    }

}