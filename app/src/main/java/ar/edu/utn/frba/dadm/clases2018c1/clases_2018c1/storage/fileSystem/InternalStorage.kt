package ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1.storage.fileSystem

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class InternalStorage {
    companion object {
        fun getFileUri(context: Context, fileName: String): String? {
            val file = File(context.filesDir.path + "/" + fileName)
            if (file.exists()) {
                return file.toURI().toString()
            }
            return null
        }

        fun saveFile(context: Context, bitmap: Bitmap, fileName: String) : File {
            val file = File(context.filesDir.path + "/" + fileName)
            try {
                file.createNewFile()
                val ostream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream)
                ostream.flush()
                ostream.close()
            } catch (e: IOException) {
                Log.e("IOException", e.localizedMessage)
            }

            return file
        }

        fun getCacheFileUri(context: Context, fileName: String): String? {
            val file = File(context.cacheDir.path + "/" + fileName)
            if (file.exists()) {
                return file.toURI().toString()
            }
            return null
        }

        fun saveFileInCache(context: Context, bitmap: Bitmap, fileName: String) : File {
            val file = File(context.cacheDir.path + "/" + fileName)
            try {
                file.createNewFile()
                val ostream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream)
                ostream.flush()
                ostream.close()
            } catch (e: IOException) {
                Log.e("IOException", e.localizedMessage)
            }

            return file
        }
    }
}