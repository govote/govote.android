package br.com.govote.android.infrastructure.di

import com.facebook.CallbackManager
import org.koin.dsl.module

val facebookModule = module {
  single { CallbackManager.Factory.create() }
}
