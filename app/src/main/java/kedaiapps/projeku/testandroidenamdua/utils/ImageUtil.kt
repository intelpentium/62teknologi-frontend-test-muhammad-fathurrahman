package kedaiapps.projeku.testandroidenamdua.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.ImageView
import androidx.fragment.app.Fragment
import coil.api.load
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

//fun openImageChooser(context: Context, activity: Activity, codeRequest : Int) {
//    val intent = Intent(context, AlbumSelectActivity::class.java)
//    intent.putExtra(Constants.INTENT_EXTRA_IMAGES, 10)
//    activity.startActivityForResult(intent, codeRequest)
//}

fun openImageChooser(fragment: Fragment, title: String) {
    CropImage.activity()
        .setActivityTitle(title)
        .setAllowFlipping(false)
        .setCropMenuCropButtonTitle("Selesai")
        .setGuidelines(CropImageView.Guidelines.ON)
        .start(fragment.requireContext(), fragment)
}

fun openImageChooserActivity(activity: Activity, title: String) {
    CropImage.activity()
        .setActivityTitle(title)
        .setAllowFlipping(false)
        .setCropMenuCropButtonTitle("Selesai")
        .setGuidelines(CropImageView.Guidelines.ON)
        .start(activity)
}

inline fun handleImageChooserResult(
    requestCode: Int,
    resultCode: Int,
    data: Intent?,
    onResult: (Uri?, error: String?) -> Unit
) {
    if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
        val result = CropImage.getActivityResult(data)
        if (resultCode == Activity.RESULT_OK) {
            onResult(result.uri, null)
        } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            onResult(null, result.error.toString())
        }
    }
}

fun bitmapToFile(bitmap:Bitmap, context: Context): Uri {
    // Get the context wrapper
    val wrapper = ContextWrapper(context)

    // Initialize a new file instance to save bitmap object
    var file = wrapper.getDir("Images", Context.MODE_PRIVATE)
    file = File(file,"${UUID.randomUUID()}.jpg")

    try{
        // Compress the bitmap and save in jpg format
        val stream: OutputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream)
        stream.flush()
        stream.close()
    }catch (e: IOException){
        e.printStackTrace()
    }

    // Return the saved bitmap uri
    return Uri.parse(file.absolutePath)
}

fun compressImage(imageFile: File, destWidth: Int = 1080): File  {
    val b = BitmapFactory.decodeFile(imageFile.absolutePath)

    val origWidth = b.width
    val origHeight = b.height

    if (origWidth > destWidth) {
        val destHeight = origHeight / (origWidth / destWidth.toFloat())
        val b2 = Bitmap.createScaledBitmap(b, destWidth, destHeight.toInt(), false)
        val outStream = ByteArrayOutputStream()
        b2.compress(Bitmap.CompressFormat.JPEG, 70, outStream)
        val fileName = imageFile.absolutePath.replace(
            imageFile.name,
            "${imageFile.nameWithoutExtension}-compressed.${imageFile.extension}"
        )
        val f = File(fileName)
        f.createNewFile()
        //write the bytes in file
        val fo = FileOutputStream(f)
        fo.write(outStream.toByteArray())
        // remember close de FileOutput
        fo.close()

        b.recycle()
        b2.recycle()
        return f
    }

    b.recycle()
    return imageFile
}

fun ImageView.loadUrlImage(uri: String?) {
    val toHttps :String? = uri?.replace("http://", "https://")
    this.load(toHttps) {
        crossfade(true)
    }
}


fun getDateNow():String{
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val now = Date()
    return sdf.format(now)
}