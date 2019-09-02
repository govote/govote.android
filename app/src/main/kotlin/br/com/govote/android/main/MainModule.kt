package br.com.govote.android.main

import org.koin.dsl.module

val mainModule = module {
  factory { (activity: MainActivity) -> DoubleBackClickToExitBehaviour(activity) }
  factory { (view: MainView) -> MainPresenter(view, get(), get()) }
  factory { (activity: MainActivity) -> OnTabChangeBehaviour(activity) }
}
