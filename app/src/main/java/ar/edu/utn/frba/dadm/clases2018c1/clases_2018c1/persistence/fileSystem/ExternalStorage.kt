package ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1.persistence.fileSystem

import android.graphics.Bitmap
import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ExternalStorage {
    companion object {
        fun getFileUri(fileName: String): String? {
            val file = File(Environment.getExternalStorageDirectory().path + "/" + fileName)
            if (file.exists()) {
                return file.toURI().toString()
            }
            return null
        }

        fun saveFile(bitmap: Bitmap, fileName: String) : File {
            val file = File(Environment.getExternalStorageDirectory().path + "/" + fileName)
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