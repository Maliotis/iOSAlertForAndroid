package com.maliotis.iosalert

import android.content.res.Resources
import android.util.TypedValue

/**
 * Created by petrosmaliotis on 27/05/2020.
 */

fun dpToPixel(dp: Float, resources: Resources): Float {
    val r: Resources = resources
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        r.displayMetrics
    )

}