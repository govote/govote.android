package br.com.govote.android.main

import dagger.Module
import dagger.Provides

@Module
internal class MainModule {

  @Provides
  fun provideDoubleBackExitBehaviour(activity: MainActivity) = DoubleBackExitBehaviour(activity)
}
