package com.maliotis.iosalert.blurview;

import android.graphics.Bitmap;


class NoOpBlurAlgorithm implements BlurAlgorithm {
    @Override
    public Bitmap blur(Bitmap bitmap, float blurRadius) {
        return bitmap;
    }

    @Override
    public void destroy() {
    }

    @Override
    public boolean canModifyBitmap() {
        return true;
    }


    @Override
    public Bitmap.Config getSupportedBitmapConfig() {
        return Bitmap.Config.ARGB_8888;
    }
}
