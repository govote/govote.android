package br.com.govote.android.infrastructure.di

import com.facebook.CallbackManager
import dagger.Module
import dagger.Provides

@Module
class FacebookModule {

  @Provides
  fun provideCallbackManager(): CallbackManager {
    return CallbackManager.Factory.create()
  }
}
