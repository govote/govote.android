package br.com.govote.android

import android.app.Application
import br.com.govote.android.auth.LoginActivityProvider
import br.com.govote.android.data.DataModule
import br.com.govote.android.main.MainActivityProvider
import br.com.govote.android.onboarding.OnboardingActivityProvider
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules =
[
  AndroidSupportInjectionModule::class,
  MainActivityProvider::class,
  LoginActivityProvider::class,
  OnboardingActivityProvider::class,
  DataModule::class,
  AppModule::class
])
interface AppComponent {

  fun inject(application: GoVoteApp)

  @Component.Builder
  interface Builder {

    @BindsInstance
    fun application(application: Application): Builder

    fun build(): AppComponent
  }
}
