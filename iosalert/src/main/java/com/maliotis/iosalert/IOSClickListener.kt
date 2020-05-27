package com.maliotis.iosalert

import android.app.Dialog

/**
 * Created by petrosmaliotis on 27/05/2020.
 */
interface IOSClickListener {
    fun onClick(dialog: Dialog?) {
        dialog?.dismiss()
    }
}