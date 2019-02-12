package br.com.govote.android.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import br.com.govote.android.BuildConfig
import br.com.govote.android.GoVoteApp
import br.com.govote.android.PerBuildComponentProvider
import br.com.govote.android.api.bff.BffApi
import br.com.govote.android.api.bff.BffApiFactory
import br.com.govote.android.api.bff.Config
import br.com.govote.android.api.graphapi.GraphApi
import br.com.govote.android.api.graphapi.GraphApiConfig
import br.com.govote.android.api.graphapi.GraphApiFactory
import br.com.govote.android.coordinator.NavigationCoordinatorModule
import br.com.govote.android.data.DataModule
import com.facebook.CallbackManager
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import java.io.File
import javax.inject.Singleton

@Module(includes = [NavigationCoordinatorModule::class, DataModule::class])
internal class AppModule {
  @Provides
  @Singleton
  fun provideContext(application: Application): Context {
    return application
  }

  @Provides
  @Singleton
  fun provideSharedPreferences(application: Application): SharedPreferences {
    return application.getSharedPreferences(GoVoteApp.SHARED_PREFERENCES, Context.MODE_PRIVATE)
  }

  @Provides
  @Singleton
  fun provideGson(): Gson {
    return GsonBuilder()
      .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
      .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
      .create()
  }

  @Provides
  @Singleton
  fun provideOkBuilder(): OkHttpClient.Builder {
    return PerBuildComponentProvider.getInstance().okHttpBuilder()
  }

  @Provides
  @Singleton
  fun provideApi(okBuilder: OkHttpClient.Builder, gson: Gson): BffApi {
    val uri = BuildConfig.API_URI
    val cache = "br.com.govote.android.network.cache"
    val config = Config(uri, File(cache), cache, 50 * 1024 * 1024)

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

  @Provides
  fun provideCallbackManager(): CallbackManager {
    return CallbackManager.Factory.create()
  }
}
