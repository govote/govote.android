package br.com.govote.android.api.graphapi

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

object GraphApiFactory {
  private var graphApi: GraphApi? = null

  operator fun get(graphApiConfig: GraphApiConfig, okBuilder: OkHttpClient.Builder): GraphApi {
    if (graphApi == null) {
      graphApi = build(graphApiConfig, okBuilder)
    }

    return graphApi as GraphApi
  }

  private fun build(graphApiConfig: GraphApiConfig, okBuilder: OkHttpClient.Builder): GraphApi {
    val httpCacheDirectory = File(graphApiConfig.cacheDir)
    val cache = Cache(httpCacheDirectory, graphApiConfig.cacheSize.toLong())

    okBuilder.cache(cache)

    return Retrofit.Builder()
      .baseUrl(graphApiConfig.uri)
      .addConverterFactory(GsonConverterFactory.create(provideGson()))
      .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
      .client(okBuilder.build())
      .build()
      .create(GraphApi::class.java)
  }

  private fun provideGson(): Gson =
    GsonBuilder()
      .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
      .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
      .create()
}
