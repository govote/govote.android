package br.com.govote.android.auth

import br.com.govote.android.data.authentication.AuthenticationRepository
import br.com.govote.android.data.authentication.LoginArgs
import br.com.govote.android.libs.mvp.BaseViewPresenter
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LoginPresenter @Inject constructor(
  loginView: LoginView,
  private val authenticationRepository: AuthenticationRepository
) : BaseViewPresenter<LoginView>(loginView) {

  fun loginWithFacebook(token: String) {
    addSubscription(
      authenticationRepository.authenticate(LoginArgs(token))
        .subscribeOn(Schedulers.io())
        .observeOn(mainThread())
        .subscribe({ getView().onSuccessfulLogin() }, { getView().onLoginError(it as Exception) })
    )
  }
}
