package kedaiapps.projeku.testandroidenamdua.services.rest

import kedaiapps.projeku.testandroidenamdua.services.Response
import kedaiapps.projeku.testandroidenamdua.services.entity.*
import retrofit2.http.*

interface MainRest {

    //home
    @GET("businesses/search")
    suspend fun home(
        @Query("location") location: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ) : ResponseHome

    //detail
    @GET("businesses/{id}")
    suspend fun homeDetail(
        @Path("id") id: String,
    ) : ResponseHomeDetail

    //reviews
    @GET("businesses/{id}/reviews")
    suspend fun homeDetailReviews(
        @Path("id") id: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ) : ResponseHomeDetailReviews
}