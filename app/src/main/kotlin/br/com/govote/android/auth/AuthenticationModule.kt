package br.com.govote.android.auth

import dagger.Module
import dagger.Provides

@Module
class AuthenticationModule {

  @Provides
  fun provideLoginView(loginActivity: LoginActivity): LoginView = loginActivity
}
