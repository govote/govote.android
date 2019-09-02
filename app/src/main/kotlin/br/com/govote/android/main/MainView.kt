package br.com.govote.android.main

import br.com.govote.android.libs.mvp.PresenterView

interface MainView : PresenterView {

  fun onAuthenticationNeeded()

  fun onAuthenticated()

  fun showOnboarding()
}
