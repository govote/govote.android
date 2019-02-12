package br.com.govote.android.authentication

import android.content.Context
import br.com.govote.android.utils.PresenterView

interface LoginView : PresenterView {

  fun onSuccessfulLogin()

  fun onError()
}
