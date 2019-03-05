package br.com.govote.android.main

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityProvider {

  @ContributesAndroidInjector(modules = [MainModule::class])
  abstract fun contributeMainActivity(): MainActivity
}
