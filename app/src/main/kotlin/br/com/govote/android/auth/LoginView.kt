package br.com.govote.android.auth

import br.com.govote.android.libs.mvp.PresenterView

interface LoginView : PresenterView {

  fun onSuccessfulLogin()

  fun onLoginError(exception: Exception)
}
