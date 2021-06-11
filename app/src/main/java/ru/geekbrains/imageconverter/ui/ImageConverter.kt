package ru.geekbrains.imageconverter.ui

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.geekbrains.imageconverter.data.Converter
import ru.geekbrains.imageconverter.data.Image
import ru.geekbrains.imageconverter.utils.DEFAULT_IMAGE_NAME
import ru.geekbrains.imageconverter.utils.IMAGE_QUALITY
import ru.geekbrains.imageconverter.utils.PNG
import ru.geekbrains.imageconverter.utils.TIME_IO_THREAD_SLEEP
import java.io.File
import java.io.FileOutputStream

class ImageConverter(private val context: Context) : Converter {
    override fun convert(image: Image?): Completable = Completable.fromAction {
        try {
            Thread.sleep(TIME_IO_THREAD_SLEEP)
        } catch (e: InterruptedException) {
            return@fromAction
        }

        val imageName = "${image?.imageName ?: DEFAULT_IMAGE_NAME}$PNG"
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), imageName)
        FileOutputStream(file).use { stream ->
            image?.bitmap?.compress(Bitmap.CompressFormat.PNG, IMAGE_QUALITY, stream)
        }
    }.subscribeOn(Schedulers.io())
}