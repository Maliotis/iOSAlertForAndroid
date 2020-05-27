package com.maliotis.iosalert.blurview;

import android.graphics.Bitmap;




public interface BlurAlgorithm {
    /**
     * @param bitmap     bitmap to be blurred
     * @param blurRadius blur radius
     * @return blurred bitmap
     */
    Bitmap blur(Bitmap bitmap, float blurRadius);

    /**
     * Frees allocated resources
     */
    void destroy();

    /**
     * @return true if this algorithm returns the same instance of bitmap as it accepted
     * false if it creates a new instance.
     * <p>
     * If you return false from this method, you'll be responsible to swap bitmaps in your
     * {@link BlurAlgorithm#blur(Bitmap, float)} implementation
     * (assign input bitmap to your field and return the instance algorithm just blurred).
     */
    boolean canModifyBitmap();

    /**
     * Retrieve the {@link Bitmap.Config} on which the {@link BlurAlgorithm}
     * can actually work.
     *
     * @return bitmap config supported by the given blur algorithm.
     */

    Bitmap.Config getSupportedBitmapConfig();
}
