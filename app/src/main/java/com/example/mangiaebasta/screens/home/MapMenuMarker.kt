package com.example.mangiaebasta.screens.home

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import androidx.compose.runtime.Composable


@Composable
fun RoundedMarkerWithBorder(
    bitmap: Bitmap,
    borderColor: Int = Color.parseColor("#080808"),
    borderWidth: Float = 8f
): Bitmap {
    val roundedBitmap = Bitmap.createBitmap(
        bitmap.width,
        bitmap.height,
        Bitmap.Config.ARGB_8888
    )

    val canvas = android.graphics.Canvas(roundedBitmap)
    val paint = Paint().apply {
        isAntiAlias = true
    }

    val radius = (bitmap.width / 2f).coerceAtMost(bitmap.height / 2f) // Raggio massimo basato sulle dimensioni
    val borderOffset = borderWidth / 2f // Offset per il bordo

    // Disegna il bordo
    paint.color = borderColor
    paint.style = Paint.Style.STROKE
    paint.strokeWidth = borderWidth
    canvas.drawCircle(
        bitmap.width / 2f, // Centro X
        bitmap.height / 2f, // Centro Y
        radius - borderOffset, // Raggio del bordo
        paint
    )

    // Disegna l'immagine arrotondata
    val path = android.graphics.Path().apply {
        addCircle(
            bitmap.width / 2f, // Centro X
            bitmap.height / 2f, // Centro Y
            radius - borderWidth, // Raggio dell'immagine
            android.graphics.Path.Direction.CW
        )
    }
    canvas.clipPath(path)
    canvas.drawBitmap(bitmap, 0f, 0f, null)

    return roundedBitmap
}

