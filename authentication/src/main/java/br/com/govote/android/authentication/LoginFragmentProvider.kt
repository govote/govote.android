package br.com.govote.android.authentication

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class LoginFragmentProvider {

  @ContributesAndroidInjector(modules = [AuthenticationModule::class])
  abstract fun bindLoginFragment(): LoginFragment
}
