package com.nahid.meetmax.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.RectF
import android.util.Base64
import android.util.Log
import java.io.ByteArrayOutputStream
import kotlin.math.max


private const val TAG = "ImageCompressor"

const val PREFERRED_FARMER_IMAGE_SIZE = 400  //400kb
const val PREFERRED_CONTRACT_IMAGE_SIZE = 1024  //400kb
const val ONE_MB_TO_KB = 1024

object ImageUtils {
    fun reduceImageSizeFromBitmapToBase64(imageBitmap: Bitmap, isContract: Boolean = true): String {

        val selectedImageBitMap: Bitmap = imageBitmap
        val baos = ByteArrayOutputStream()
        selectedImageBitMap.compress(Bitmap.CompressFormat.JPEG, 50, baos)
        val byteArray = baos.toByteArray()

        val encodedImage =
            if (byteArray.size / ONE_MB_TO_KB > if (isContract) PREFERRED_CONTRACT_IMAGE_SIZE else PREFERRED_FARMER_IMAGE_SIZE) {//resize photo & set Image in imageview In UI
                val imageInBitMapAfterResize: Bitmap = resizePhoto(selectedImageBitMap)
                val resizedBAOS = ByteArrayOutputStream()
                imageInBitMapAfterResize.compress(Bitmap.CompressFormat.JPEG, 100, resizedBAOS)
                val resizedByteArray: ByteArray = resizedBAOS.toByteArray()
                val encoded = Base64.encodeToString(resizedByteArray, Base64.DEFAULT)
                encoded
            } else {
                val encoded = Base64.encodeToString(byteArray, Base64.DEFAULT)
                encoded
            }

        return encodedImage
    }


    private fun resizePhoto(bitmap: Bitmap): Bitmap {
        try {
            val w = bitmap.width
            val h = bitmap.height
            val aspRat = w / h
            val W = 400
            val H = W * aspRat
            val b = Bitmap.createScaledBitmap(bitmap, W, H, false)

            return b
        } catch (e: Exception) {
            Log.d(TAG, "resizePhoto: ${e.message}")
            return bitmap
        }
    }

    fun scaleCenterCrop(source: Bitmap, newHeight: Int, newWidth: Int): Bitmap {
        val sourceWidth = source.width
        val sourceHeight = source.height

        // Compute the scaling factors to fit the new height and width, respectively.
        // To cover the final image, the final scaling will be the bigger
        // of these two.
        val xScale = newWidth.toFloat() / sourceWidth
        val yScale = newHeight.toFloat() / sourceHeight
        val scale = max(xScale.toDouble(), yScale.toDouble()).toFloat()

        // Now get the size of the source bitmap when scaled
        val scaledWidth = scale * sourceWidth
        val scaledHeight = scale * sourceHeight

        // Let's find out the upper left coordinates if the scaled bitmap
        // should be centered in the new size give by the parameters
        val left = (newWidth - scaledWidth) / 2
        val top = (newHeight - scaledHeight) / 2

        // The target rectangle for the new, scaled version of the source bitmap will now
        // be
        val targetRect = RectF(left, top, left + scaledWidth, top + scaledHeight)

        // Finally, we create a new bitmap of the specified size and draw our new,
        // scaled bitmap onto it.
        val dest = Bitmap.createBitmap(newWidth, newHeight, source.config)
        val canvas = Canvas(dest)
        canvas.drawBitmap(source, null, targetRect, null)

        return dest
    }

    fun rotateImageBitmap(bitmap: Bitmap, angle: Float, width: Int, height: Int): Bitmap {
        val matrix: Matrix = Matrix()

        matrix.postRotate(angle)

        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true)

        val rotatedBitmap = Bitmap.createBitmap(
            scaledBitmap,
            0,
            0,
            scaledBitmap.width,
            scaledBitmap.height,
            matrix,
            true
        )

        return rotatedBitmap
    }

    fun base64StringToBitmap(image: String?): Bitmap? {
        if (image.isNullOrEmpty()) {
            return null // Or return a default image, if you want
        }

        return try {
            val decodedString = Base64.decode(image, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        } catch (e: Exception) {
            e.printStackTrace()
            null // Return null if there's any error decoding
        }
    }

}