package br.com.govote.android.di

import br.com.govote.android.MainActivity
import br.com.govote.android.authentication.LoginFragmentProvider
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(includes = [LoginFragmentProvider::class])
abstract class ActivitiesBuilder {
  @ContributesAndroidInjector
  abstract fun contributeMainActivity(): MainActivity
}
