package br.com.govote.android.data

import android.content.SharedPreferences
import br.com.govote.android.api.bff.BffApi
import br.com.govote.android.api.graphapi.GraphApi
import br.com.govote.android.data.authentication.AuthenticationRepository
import br.com.govote.android.data.onboarding.OnboardingRepository
import dagger.Module
import dagger.Provides

@Module
class DataModule {

  @Provides
  fun provideAuthenticationRepository(
    graphApi: GraphApi,
    bffApi: BffApi,
    sharedPreferences: SharedPreferences
  ): AuthenticationRepository =
    AuthenticationRepository(graphApi, bffApi, sharedPreferences)

  @Provides
  fun provideOnboardingRepository(
    sharedPreferences: SharedPreferences
  ): OnboardingRepository =
    OnboardingRepository(sharedPreferences)
}
