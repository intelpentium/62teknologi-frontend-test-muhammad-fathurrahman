package kedaiapps.projeku.testandroidenamdua.utils

import android.annotation.TargetApi
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.buffer
import okio.sink
import java.io.File
import javax.inject.Inject

class DownloadRepository @Inject constructor (
  @ApplicationContext val context: Context
) {

  suspend fun download(url: String, filename: String, mimeType: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      downloadQ(url, filename, mimeType)
    } else {
      downloadLegacy(url, filename, mimeType)
    }
  }

  @TargetApi(29)
  private suspend fun downloadQ(
    url: String,
    filename: String,
    mimeType: String
  ) = withContext(Dispatchers.IO) {
      val response = OkHttpClient().newCall(Request.Builder().url(url).build()).execute()

      if (response.isSuccessful) {
        val values = ContentValues().apply {
          put(MediaStore.Downloads.DISPLAY_NAME, filename)
          put(MediaStore.Downloads.MIME_TYPE, mimeType)
          put(MediaStore.Downloads.IS_PENDING, 1)
        }

        val resolver = context.contentResolver
        val uri =
          resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)

        uri?.let {
          resolver.openOutputStream(uri)?.use { outputStream ->
            val sink = outputStream.sink().buffer()

            response.body?.source()?.let { sink.writeAll(it) }
            sink.close()
          }

          values.clear()
          values.put(MediaStore.Downloads.IS_PENDING, 0)

          resolver.update(uri, values, null, null)
        } ?: throw RuntimeException("MediaStore failed for some reason")

      } else {
        throw RuntimeException("OkHttp failed for some reason")
      }
    }

  private suspend fun downloadLegacy(
    url: String,
    filename: String,
    mimeType: String
  ) = withContext(Dispatchers.IO) {
      val file = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
        filename
      )
      val response = OkHttpClient().newCall(Request.Builder().url(url).build()).execute()

      if (response.isSuccessful) {
        val sink = file.sink().buffer()

        response.body?.source()?.let { sink.writeAll(it) }
        sink.close()

        MediaScannerConnection.scanFile(
          context,
          arrayOf(file.absolutePath),
          arrayOf(mimeType),
          null
        )
      } else {
        throw RuntimeException("OkHttp failed for some reason")
      }
    }

  private fun <T : Any> Cursor.mapToList(predicate: (Cursor) -> T): List<T> =
    generateSequence { if (moveToNext()) predicate(this) else null }
      .toList()
}