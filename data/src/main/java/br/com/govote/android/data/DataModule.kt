package br.com.govote.android.data

import android.content.SharedPreferences
import br.com.govote.android.api.bff.BffApi
import br.com.govote.android.api.graphapi.GraphApi
import br.com.govote.android.data.authentication.AuthenticationRepository
import br.com.govote.android.data.onboarding.OnboardingRepository
import org.koin.dsl.module

val dataModule = module {
  factory { authenticationRepository(get(), get(), get()) }
  factory { onboardingRepository(get()) }
}

private fun authenticationRepository(
  graphApi: GraphApi,
  bffApi: BffApi,
  sharedPreferences: SharedPreferences
): AuthenticationRepository =
  AuthenticationRepository(graphApi, bffApi, sharedPreferences)

private fun onboardingRepository(
  sharedPreferences: SharedPreferences
): OnboardingRepository =
  OnboardingRepository(sharedPreferences)
