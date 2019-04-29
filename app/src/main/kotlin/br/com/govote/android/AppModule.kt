package br.com.govote.android

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import br.com.govote.android.data.DataModule
import br.com.govote.android.infrastructure.di.ApiModule
import br.com.govote.android.infrastructure.di.ConvertersModule
import br.com.govote.android.infrastructure.di.FacebookModule
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes =
[
  DataModule::class,
  ApiModule::class,
  ConvertersModule::class,
  FacebookModule::class,
  PerBuildModule::class
])
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
}
