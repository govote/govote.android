package br.com.govote.android.di

import android.app.Application
import br.com.govote.android.GoVoteApp
import br.com.govote.android.authentication.AuthenticationModule
import br.com.govote.android.data.DataModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidInjectionModule::class, ActivitiesBuilder::class, DataModule::class, AppModule::class])
interface AppComponent {
  fun inject(application: GoVoteApp)

  @Component.Builder
  interface Builder {
    @BindsInstance
    fun application(application: Application): Builder

    fun build(): AppComponent
  }
}
