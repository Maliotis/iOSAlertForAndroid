package com.maliotis.ioslikealert

import android.app.Dialog
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import com.maliotis.iosalert.IOSAlert
import com.maliotis.iosalert.IOSClickListener


val TAG = MainActivity::class.java.simpleName
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        IOSAlert.Builder(this)
            .title("Title")
            .body("This is a body text")
            .typeface(Typeface.SERIF)
            .iOSAlertPositiveClickListener(object: IOSClickListener {
                override fun onClick(dialog: Dialog?) {
                    // Your code here on Positive Click
                    Log.d(TAG, "onClick: positive button pressed")
                    dialog?.dismiss()
                }
            })
            .negativeText("Cancel")
            .iOSAlertNegativeClickListener(object: IOSClickListener {
                override fun onClick(dialog: Dialog?) {
                    // Your code here on Negative Click
                    Log.d(TAG, "onClick: negative button pressed")
                    dialog?.dismiss()
                }
            })
            // The blur radius <= 25f
            .blurRadius(22f)
            // Sets the transparency to 0.2f - that translates to 0.8 alpha value
            .transparency(0.2f)
            // On touch down will highlight the buttons with a gray tint
            .tintButtons(true)
            //.tintButtonsColor(Color.DKGRAY)
            //.backgroundColor(Color.WHITE)
            //.backgroundColor(255, 255, 255, 255)
            //.cornerRadius(10f) // 10f  by default

            // User won't be able to leave the alert without pressing one of the buttons
            .isCancellable(false)


            //.build() // Use build instead of buildAndShow if you want to show the alert yourself
                       // In the case use: iosAlert.show(supportFragmentManager, "tag")
            .buildAndShow()

        //iosAlert.show(supportFragmentManager, "tag")
    }
}
