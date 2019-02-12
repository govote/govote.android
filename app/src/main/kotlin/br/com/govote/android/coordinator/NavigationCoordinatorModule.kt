package br.com.govote.android.coordinator

import br.com.govote.android.authentication.LoginNavigation
import br.com.govote.android.coordinator.authentication.LoginNavigator
import dagger.Module
import dagger.Provides

@Module
class NavigationCoordinatorModule {
  @Provides
  fun provideLoginNavigation(): LoginNavigation {
    return LoginNavigator()
  }
}
