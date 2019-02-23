package br.com.govote.android.main

import br.com.govote.android.authentication.LoginFragmentProvider
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(includes = [LoginFragmentProvider::class])
abstract class MainActivityProvider {

  @ContributesAndroidInjector
  abstract fun contributeMainActivity(): MainActivity
}
