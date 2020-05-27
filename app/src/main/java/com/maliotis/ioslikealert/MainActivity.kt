package com.maliotis.ioslikealert

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import com.maliotis.iosalert.IOSAlert
import com.maliotis.iosalert.IOSClickListener



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val iosAlert = IOSAlert.Builder(this)
            .title("title")
            .body("body")
            .blurRadius(25f)
            .iOSAlertPositiveClickListener(object: IOSClickListener {
                override fun onClick(dialog: Dialog?) {
                    // Your code here on Positive Click
                    dialog?.dismiss()
                }

            })
            .negativeText("Done")
            .transparency(0.0f)
            .isCancellable(false)
            .buildAndShow()

        //iosAlert.show(supportFragmentManager, "tag")
    }
}
