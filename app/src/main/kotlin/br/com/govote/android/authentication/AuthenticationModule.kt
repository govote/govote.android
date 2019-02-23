package br.com.govote.android.authentication

import dagger.Module
import dagger.Provides

@Module
class AuthenticationModule {

  @Provides
  fun provideLoginView(loginFragment: LoginFragment) : LoginView = loginFragment
}
