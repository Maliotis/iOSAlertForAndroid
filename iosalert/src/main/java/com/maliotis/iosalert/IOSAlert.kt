package com.maliotis.iosalert

import android.app.Activity
import android.app.Dialog
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewOutlineProvider
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColor
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
    var backgroundColor: Int? = null

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
        var backgroundColor: Int? = null
        var isCancellable: Boolean = true
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

        /**
         * Will build and return the iOSAlert instance with the parameters specified in the Builder
         */
        fun build() : IOSAlert {
            iosAlert = IOSAlert(activity, title, body, typeface, positiveText, negativeText,iosPositiveClickListener, iosNegativeClickListener)
            iosAlert!!.blurRadius = blurRadius
            iosAlert!!.backgroundColor = backgroundColor
            iosAlert!!.isCancelable = isCancellable
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
        val okButton: Button = v.findViewById(R.id.popUpOkButton)
        var negativeButton: Button? = null

        if (body.isNullOrEmpty()) blurView.findViewById<LinearLayout>(R.id.layoutInsideBlurView).removeView(bodyTextView)
        else bodyTextView.text = body

        title = title ?: ""
        titleTextView.text = title
        positiveText = positiveText ?: "OK"

        val positiveClickListener = getOkButtonClickListener()
        if (negativeText != null || iosNegativeClickListener != null) {
            // Add a negative button
            negativeButton = getNegativeButton()
            val lineSeperator = getLineSeparator()
            linearLayout.addView(lineSeperator, 0)
            linearLayout.addView(negativeButton, 0)

        }
        okButton.setOnClickListener(positiveClickListener)
        // set typeface

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Blur background

        val decorView: View = activity1.window.decorView
        val rootView = decorView.findViewById<View>(android.R.id.content) as ViewGroup
        val windowBackground = decorView.background

        if (backgroundColor != null) {
            val backgroundDrawable = GradientDrawable()
            backgroundDrawable.cornerRadius = dpToPixel(10f)
            backgroundDrawable.setColor(backgroundColor!!)
            blurView.background = backgroundDrawable
        }
        
        blurView.setupWith(rootView)
            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(RenderScriptBlur(activity1))
            .setBlurRadius(blurRadius)
            .setHasFixedTransformationMatrix(true)

        blurView.outlineProvider = ViewOutlineProvider.BACKGROUND
        blurView.clipToOutline = true

        return v
    }

    private fun getOkButtonClickListener(): View.OnClickListener {
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

    private fun getNegativeButton(): Button {
        return Button(activity1).apply {
            setTextColor(Color.RED)
            text = negativeText ?: "CANCEL"
            if (this@IOSAlert.typeface != null)
                typeface = this@IOSAlert.typeface

            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, dpToPixel(44f).toInt(), 0.5f)
            setBackgroundColor(Color.TRANSPARENT)
            setOnClickListener(getNegativeButtonClickListener())
        }
    }

    private fun getLineSeparator(): View {
        return View(activity1).apply {
            layoutParams = LinearLayout.LayoutParams(dpToPixel(0.5f).toInt(), MATCH_PARENT)
            setBackgroundColor(Color.parseColor("#9FAAAAAA"))

        }
    }

    private fun dpToPixel(dp: Float): Float {
        val r: Resources = resources
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            r.displayMetrics
        )

    }

}

interface IOSClickListener {
    fun onClick(dialog: Dialog?) {
        dialog?.dismiss()
    }
}