package kedaiapps.projeku.testandroidenamdua.services.entity

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

//home
@Keep
data class ResponseHome(
    @SerializedName("businesses") val businesses: List<ResponseHomeItem>,
    @SerializedName("total") val total: Int,
)

@Keep
data class ResponseHomeItem(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("rating") val rating: String,
    @SerializedName("review_count") val review_count: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("display_phone") val display_phone: String,
    @SerializedName("distance") val distance: String,
    @SerializedName("image_url") val image_url: String,
)

//home detail
@Keep
data class ResponseHomeDetail(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("rating") val rating: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("display_phone") val display_phone: String,
    @SerializedName("photos") val photos: List<String>,
    @SerializedName("coordinates") val coordinates: ResponseCoordinates,
)

@Keep
data class ResponseCoordinates(
    @SerializedName("latitude") val latitude: String,
    @SerializedName("longitude") val longitude: String,
)


//home Reviews
@Keep
data class ResponseHomeDetailReviews(
    @SerializedName("reviews") val reviews: List<ResponseHomeDetailReviewsItem>,
    @SerializedName("total") val total: Int,
)

@Keep
data class ResponseHomeDetailReviewsItem(
    @SerializedName("text") val text: String,
    @SerializedName("rating") val rating: String,
    @SerializedName("user") val user: ResponseHomeDetailReviewsUsers,
)

@Keep
data class ResponseHomeDetailReviewsUsers(
    @SerializedName("id") val id: String,
    @SerializedName("profile_url") val profile_url: String,
    @SerializedName("image_url") val image_url: String,
    @SerializedName("name") val name: String,
)


