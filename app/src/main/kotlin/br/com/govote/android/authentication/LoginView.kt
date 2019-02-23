package br.com.govote.android.authentication

import br.com.govote.android.libs.mvp.PresenterView

interface LoginView : PresenterView {

  fun onSuccessfulLogin()

  fun onLoginError()
}
