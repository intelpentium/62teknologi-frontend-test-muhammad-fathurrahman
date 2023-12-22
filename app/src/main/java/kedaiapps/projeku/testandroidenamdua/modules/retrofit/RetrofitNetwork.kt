package kedaiapps.projeku.testandroidenamdua.modules.retrofit

import android.util.Log
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kedaiapps.projeku.testandroidenamdua.common.storage.Preferences
import kedaiapps.projeku.testandroidenamdua.services.rest.MainRest
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kedaiapps.projeku.testandroidenamdua.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
class RetrofitNetwork {
    private val timeOut: Int by lazy {
        180
    }

    private val responseInterceptor by lazy {
        ResponseInterceptor
    }

    private val jsonChecker by lazy {
        object : Converter.Factory() {
            override fun responseBodyConverter(
                type: Type,
                annotations: Array<Annotation>,
                retrofit: Retrofit
            ): Converter<ResponseBody, *>? {
                return Converter<ResponseBody, Any> { responseBody ->
                    val delegate =
                        retrofit.nextResponseBodyConverter<Any>(this, type, annotations)
                    try {
                        delegate.convert(responseBody)
                    } catch (error: Exception) {
                        error.message?.let { Log.e("quotes ", it) }
                        throw IOException("Terjadi kesalahan pada server")
                    }
                }
            }
        }
    }

    @Provides
    fun providesOkhttpClient(
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
                .addInterceptor(responseInterceptor)
                .connectTimeout(timeOut.toLong(), TimeUnit.SECONDS)
                .readTimeout(timeOut.toLong(), TimeUnit.SECONDS)
                .writeTimeout(timeOut.toLong(), TimeUnit.SECONDS)
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(
                    HttpLoggingInterceptor()
                            .setLevel(HttpLoggingInterceptor.Level.BODY)
            )
        }
        return builder.build()
    }

    @Provides
    fun providesRetrofit(
            okHttpClient: OkHttpClient,
            preferences: Preferences
    ): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BuildConfig.API_BASE_URL)
                .addConverterFactory(jsonChecker)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
                .also {
                    responseInterceptor.retrofit = it
                    responseInterceptor.getToken = preferences.token
                }
    }

    @Provides
    fun providesMainRest(retrofit: Retrofit): MainRest {
        return retrofit.create(MainRest::class.java)
    }
}