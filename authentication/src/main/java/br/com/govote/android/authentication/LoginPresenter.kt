package br.com.govote.android.authentication

import android.content.Intent
import androidx.fragment.app.Fragment
import br.com.govote.android.data.AuthenticationRepository
import br.com.govote.android.data.LoginArgs
import br.com.govote.android.utils.BaseViewPresenter
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LoginPresenter @Inject constructor(
  loginView: LoginView,
  private val callbackManager: CallbackManager,
  private val authenticationRepository: AuthenticationRepository,
  private val loginNavigation: LoginNavigation
) : BaseViewPresenter<LoginView>(loginView), FacebookCallback<LoginResult> {

  private val FB_PERMISSIONS = listOf("public_profile", "email", "user_photos", "user_birthday")

  init {
    LoginManager.getInstance().registerCallback(callbackManager, this)
  }

  fun loginWithFacebook(fragment: Fragment) {
    LoginManager.getInstance().logInWithReadPermissions(fragment, FB_PERMISSIONS)
  }

  fun result(requestCode: Int, resultCode: Int, data: Intent?) {
    callbackManager.onActivityResult(requestCode, resultCode, data)
  }

  override fun onSuccess(result: LoginResult?) {
    addSubscription(authenticationRepository.authenticate(LoginArgs(result?.getAccessToken()?.token.orEmpty()))
      .subscribeOn(Schedulers.io())
      .observeOn(mainThread())
      .subscribe {
        getView().onSuccessfulLogin()
      })
  }

  override fun onCancel() {

  }

  override fun onError(error: FacebookException?) {
    getView().onError()
  }

}
