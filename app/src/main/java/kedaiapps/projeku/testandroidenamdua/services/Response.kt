package kedaiapps.projeku.testandroidenamdua.services

import androidx.annotation.Keep

import com.google.gson.annotations.SerializedName


class Response<T> (
    @SerializedName("businesses") val data: T,
    @SerializedName("total") val total: Int
)

@Keep
data class SuccessResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String
)

@Keep
data class ErrorResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String
)

@Keep
data class ResponseAuth(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String,
    @SerializedName("token") val token: String
)

@Keep
data class ResponseRegister(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String,
    @SerializedName("active_number") val active_number: Int
)


//preview
@Keep
data class ResponsePreview(
    @SerializedName("preview_stat") val preview_stat: Boolean,
)