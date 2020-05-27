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



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        IOSAlert.Builder(this)
            .title("Title")
            .body("This is a body text")
            .iOSAlertPositiveClickListener(object: IOSClickListener {
                override fun onClick(dialog: Dialog?) {
                    // Your code here on Positive Click
                    dialog?.dismiss()
                }

            })
            .buildAndShow()

        //iosAlert.show(supportFragmentManager, "tag")
    }
}
