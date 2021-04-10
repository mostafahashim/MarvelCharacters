package com.marvel.characters.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class BitmapHelper {
    //    private static final String TAG = LogHelper.makeLogTag(BitmapHelper.class);

    // Max read limit that we allow our input stream to mark/reset.
    private val MAX_READ_LIMIT_PER_IMG = 1024 * 1024

    fun scaleBitmap(src: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
        val scaleFactor = Math.min(
            maxWidth.toDouble() / src.width, maxHeight.toDouble() / src.height
        )
        return Bitmap.createScaledBitmap(
            src,
            (src.width * scaleFactor).toInt(), (src.height * scaleFactor).toInt(), false
        )
    }

    fun scaleBitmap(scaleFactor: Int, `is`: InputStream): Bitmap? {
        // Get the dimensions of the bitmap
        val bmOptions = BitmapFactory.Options()

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false
        bmOptions.inSampleSize = scaleFactor

        return BitmapFactory.decodeStream(`is`, null, bmOptions)
    }

    fun findScaleFactor(targetW: Int, targetH: Int, `is`: InputStream): Int {
        // Get the dimensions of the bitmap
        val bmOptions = BitmapFactory.Options()
        bmOptions.inJustDecodeBounds = true
        BitmapFactory.decodeStream(`is`, null, bmOptions)
        val actualW = bmOptions.outWidth
        val actualH = bmOptions.outHeight

        // Determine how much to scale down the image
        return Math.min(actualW / targetW, actualH / targetH)
    }

    @Throws(IOException::class)
    fun fetchAndRescaleBitmap(uri: String, width: Int, height: Int): Bitmap? {
        val url = URL(uri)
        var `is`: BufferedInputStream? = null
        try {
            val urlConnection = url.openConnection() as HttpURLConnection
            `is` = BufferedInputStream(urlConnection.inputStream)
            `is`.mark(MAX_READ_LIMIT_PER_IMG)
            val scaleFactor = findScaleFactor(width, height, `is`)
            //            LogHelper.d(TAG, "Scaling bitmap ", uri, " by factor ", scaleFactor, " to support ",
            //                    width, "x", height, "requested dimension");
            `is`.reset()
            return scaleBitmap(scaleFactor, `is`)
        } finally {
            `is`?.close()
        }
    }
}