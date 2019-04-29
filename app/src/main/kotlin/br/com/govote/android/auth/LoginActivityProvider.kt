package br.com.govote.android.auth

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class LoginActivityProvider {

  @ContributesAndroidInjector(modules = [AuthenticationModule::class])
  abstract fun contributesLoginActivity(): LoginActivity
}
