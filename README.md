# iOSAlertForAndroid

iOSAlertForAndroid is an easy to use library for displaying ios like alerts.

## Setup
  
 Gradle:  [![](https://jitpack.io/v/Maliotis/iOSAlertForAndroid.svg)](https://jitpack.io/#Maliotis/iOSAlertForAndroid)

```gradle
repositories {
  mavenCentral()
  google()
  maven { url 'https://jitpack.io' }
}

dependencies {
  implementation 'com.github.Maliotis:iOSAlertForAndroid:${latestVersion}'
}
```

## Usage

A simple usage of the library

![Simple Alert](https://github.com/Maliotis/iOSAlertForAndroid/blob/master/app/src/main/res/drawable/simple_alert.png)

```Kotlin
IOSAlert.Builder(this)
            .title("Title")
            .body("This is a body text")
            .iOSAlertPositiveClickListener(object: IOSClickListener {
                override fun onClick(dialog: Dialog?) {
                    // Your code here on Positive Click
                    dialog?.dismiss()
                }
            })
            .isCancellable(false)
            .buildAndShow()
```

## Customization

The library allows for the following customizations:
* **Typeface**
* **Body** *optional*
* **Blur Radius** - controls the intensity if the blur. *0f < blurRadius ≤ 25f*
* **Transparency** - Sets the transparency value with a white background
* **Background Color**
* **Cancellable** - Prevents user from escaping the alert


![Custom Alert](https://github.com/Maliotis/iOSAlertForAndroid/blob/master/app/src/main/res/drawable/custom_alert.png)

```Kotlin
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
```



## License
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
