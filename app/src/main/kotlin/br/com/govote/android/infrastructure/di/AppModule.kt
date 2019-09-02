package br.com.govote.android.infrastructure.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import br.com.govote.android.GoVoteApp
import br.com.govote.android.auth.authModule
import br.com.govote.android.data.dataModule
import br.com.govote.android.main.mainModule
import br.com.govote.android.onboarding.onboardingModule
import br.com.govote.android.perBuildModule
import org.koin.core.module.Module
import org.koin.dsl.module

private val appModule = module {
  single { provideSharedPreferences(get()) }
}

val appComponents: List<Module> = listOf(
  perBuildModule,
  facebookModule,
  appModule,
  dataModule,
  apiModule,
  authModule,
  mainModule,
  onboardingModule
)

private fun provideSharedPreferences(application: Application): SharedPreferences =
  application.getSharedPreferences(GoVoteApp.SHARED_PREFERENCES, Context.MODE_PRIVATE)
