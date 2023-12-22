package kedaiapps.projeku.testandroidenamdua.ext

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

val Throwable.errorMesssage: String
    get() = message ?: "Something went wrong"


fun String.toRequestBody(): RequestBody {
    return this.toRequestBody("text/plain".toMediaTypeOrNull())
}

fun File.toRequestBody(fieldName: String, mediaType: String = "image/*"): MultipartBody.Part {
    val reqFile = this.asRequestBody(mediaType.toMediaTypeOrNull())
    return MultipartBody.Part.createFormData(fieldName, name, reqFile)
}

fun File.toRequestBody2(fieldName: String, mediaType: String = "application/*"): MultipartBody.Part {
    val reqFile = this.asRequestBody(mediaType.toMediaTypeOrNull())
    return MultipartBody.Part.createFormData(fieldName, name, reqFile)
}
