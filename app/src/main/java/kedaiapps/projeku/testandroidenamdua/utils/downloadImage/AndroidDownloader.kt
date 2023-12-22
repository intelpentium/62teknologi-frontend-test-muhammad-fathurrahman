package kedaiapps.projeku.testandroidenamdua.utils.downloadImage

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import androidx.core.net.toUri

class AndroidDownloader(
    private val context: Context
): Downloader {

    private val downloadManager = context.getSystemService(DownloadManager::class.java)

    override fun downloadFile(url: String): Long {
        val request = DownloadManager.Request(url.toUri())
            .setMimeType("image/png")
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setTitle("image.png")
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "image.png")
        return downloadManager.enqueue(request)
    }
}