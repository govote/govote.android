package br.com.govote.android.infrastructure.di

import br.com.govote.android.BuildConfig
import br.com.govote.android.api.bff.BffApi
import br.com.govote.android.api.bff.BffApiFactory
import br.com.govote.android.api.bff.BffConfig
import br.com.govote.android.api.graphapi.GraphApi
import br.com.govote.android.api.graphapi.GraphApiConfig
import br.com.govote.android.api.graphapi.GraphApiFactory
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import java.io.File
import javax.inject.Singleton

@Module
class ApiModule {

  @Provides
  @Singleton
  fun provideApi(okBuilder: OkHttpClient.Builder, gson: Gson): BffApi {
    val uri = BuildConfig.API_URI
    val cache = "br.com.govote.android.network.cache"
    val config = BffConfig(uri, File(cache), cache, 50 * 1024 * 1024)

    return BffApiFactory.build(okBuilder, gson, config)
  }

  @Provides
  @Singleton
  fun provideGraphApi(okBuilder: OkHttpClient.Builder): GraphApi {
    val uri = BuildConfig.GRAPH_API_URI
    val cache = "br.com.govote.android.network.graphapi_cache"
    val config = GraphApiConfig(uri, cache, 50 * 1024 * 1024)

    return GraphApiFactory.get(config, okBuilder)
  }
}
