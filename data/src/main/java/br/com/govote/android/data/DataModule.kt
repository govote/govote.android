package br.com.govote.android.data

import android.content.SharedPreferences
import br.com.govote.android.api.bff.BffApi
import br.com.govote.android.api.graphapi.GraphApi
import br.com.govote.android.data.authentication.AuthenticationRepository
import br.com.govote.android.data.users.UserRepository
import dagger.Module
import dagger.Provides

@Module
class DataModule {

  @Provides
  fun provideUserRepository(sharedPreferences: SharedPreferences): UserRepository =
    UserRepository(sharedPreferences)

  @Provides
  fun provideAuthenticationRepository(
    graphApi: GraphApi,
    bffApi: BffApi
  ): AuthenticationRepository =
    AuthenticationRepository(graphApi, bffApi)
}
