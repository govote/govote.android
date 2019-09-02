package br.com.govote.android.infrastructure.di

import br.com.govote.android.BuildConfig
import br.com.govote.android.api.bff.BffApi
import br.com.govote.android.api.bff.BffApiFactory
import br.com.govote.android.api.bff.BffConfig
import br.com.govote.android.api.graphapi.GraphApi
import br.com.govote.android.api.graphapi.GraphApiConfig
import br.com.govote.android.api.graphapi.GraphApiFactory
import okhttp3.OkHttpClient
import org.koin.dsl.module
import java.io.File

val apiModule = module {
  single { bffApi(get()) }
  single { graphApi(get()) }
}

private fun bffApi(okBuilder: OkHttpClient.Builder): BffApi {
  val uri = BuildConfig.API_URI
  val cache = "br.com.govote.android.network.cache"
  val config = BffConfig(uri, File(cache), cache)

  return BffApiFactory.build(okBuilder, config)
}

fun graphApi(okBuilder: OkHttpClient.Builder): GraphApi {
  val uri = BuildConfig.GRAPH_API_URI
  val cache = "br.com.govote.android.network.graphapi_cache"
  val config = GraphApiConfig(uri, cache, 50 * 1024 * 1024)

  return GraphApiFactory.get(config, okBuilder)
}
