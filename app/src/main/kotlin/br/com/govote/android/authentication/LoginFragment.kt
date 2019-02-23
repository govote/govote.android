package br.com.govote.android.authentication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import br.com.govote.android.R
import br.com.govote.android.libs.mvp.PresenterFragment
import br.com.govote.android.libs.delegates.viewIdentifiedBy
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import javax.inject.Inject

class LoginFragment : LoginView, PresenterFragment<LoginView, LoginPresenter>(),
  FacebookCallback<LoginResult> {

  private val FB_PERMISSIONS = listOf("public_profile", "email", "user_photos", "user_birthday")

  @Inject lateinit var callbackManager: CallbackManager
  private val facebookLogin: Button by viewIdentifiedBy(R.id.facebookLogin)

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? = inflater.inflate(R.layout.login, container, false)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    LoginManager.getInstance().registerCallback(callbackManager, this)

    facebookLogin.setOnClickListener {
      LoginManager.getInstance().logInWithReadPermissions(this, FB_PERMISSIONS)
    }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    callbackManager.onActivityResult(requestCode, resultCode, data)
  }

  override fun onSuccessfulLogin() {
    findNavController().popBackStack()
  }

  override fun onLoginError() {
  }

  override fun onError(error: FacebookException?) {
    onLoginError()
  }

  override fun onSuccess(result: LoginResult?) {
    viewPresenter.loginWithFacebook(result?.accessToken!!.token.orEmpty())
  }

  override fun onCancel() {
  }
}
