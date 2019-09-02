package br.com.govote.android.api.bff

import br.com.govote.android.api.ApiAdapterFactory
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.reactivex.annotations.NonNull
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

object BffApiFactory {
  private const val CACHE_SIZE = 50 * 1024 * 1024

  internal var bffApi: BffApi? = null

  operator fun get(
    @NonNull okBuilder: OkHttpClient.Builder,
    @NonNull bffConfig: BffConfig
  ): BffApi {

    if (bffApi == null) {
      bffApi = build(okBuilder, bffConfig)
    }

    return bffApi as BffApi
  }

  fun build(
    @NonNull okBuilder: OkHttpClient.Builder,
    @NonNull bffConfig: BffConfig): BffApi {
    val cacheDir = File(bffConfig.cacheDir, bffConfig.cacheName)
    val cache = Cache(cacheDir, CACHE_SIZE.toLong())

    okBuilder
      .cache(cache)
      .followRedirects(true)
      .followSslRedirects(true)

    return Retrofit.Builder()
      .baseUrl(bffConfig.uri)
      .addConverterFactory(GsonConverterFactory.create(provideGson()))
      .addCallAdapterFactory(ApiAdapterFactory())
      .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
      .client(okBuilder.build())
      .build()
      .create(BffApi::class.java)
  }

  private fun provideGson(): Gson =
    GsonBuilder()
      .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
      .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
      .create()
}
