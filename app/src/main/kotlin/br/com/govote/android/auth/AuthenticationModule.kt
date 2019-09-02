package br.com.govote.android.auth

import org.koin.dsl.module

val authModule = module {
  factory { (view: LoginView) -> LoginPresenter(view, get()) }
}
