package br.com.govote.android.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import br.com.govote.android.BuildConfig.APPLICATION_ID
import br.com.govote.android.R
import br.com.govote.android.libs.delegates.viewIdentifiedBy
import br.com.govote.android.libs.mvp.PresenterActivity
import br.com.govote.android.main.MainActivity
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class LoginActivity : PresenterActivity<LoginView, LoginPresenter>(), LoginView,
                      FacebookCallback<LoginResult> {

  override val viewPresenter: LoginPresenter by inject { parametersOf(this@LoginActivity) }

  private val facebookPermissions =
    listOf("public_profile", "email", "user_photos", "user_birthday")

  private val callbackManager: CallbackManager by inject()
  private val facebookLogin: Button by viewIdentifiedBy(R.id.facebookLogin)

  companion object {
    private const val EXTRA_ERROR_MESSAGE =
      "$APPLICATION_ID.authentication.LoginActivity.ERROR_MESSAGE"
    private const val EXTRA_IS_NEW_TASK =
      "$APPLICATION_ID.authentication.LoginActivity.NEW_TASK"

    fun newClearIntent(context: Context): Intent =
      with(Intent(context, LoginActivity::class.java)) {
        this.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        this.putExtra(EXTRA_IS_NEW_TASK, true)
        return this
      }

    fun newIntent(context: Context): Intent = Intent(context, LoginActivity::class.java)

    fun newIntentWithError(context: Context, error: String): Intent =
      with(newClearIntent(context)) {
        this.putExtra(EXTRA_ERROR_MESSAGE, error)
        return this
      }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.login)

    LoginManager.getInstance().registerCallback(callbackManager, this)

    facebookLogin.setOnClickListener {
      LoginManager.getInstance().logInWithReadPermissions(this, facebookPermissions)
    }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    callbackManager.onActivityResult(requestCode, resultCode, data)
  }

  override fun onSuccessfulLogin() {
    startActivity(MainActivity.newIntent(this))
    finish()
  }

  override fun onLoginError(exception: Exception) {
    Toast.makeText(this, exception.message, Toast.LENGTH_LONG).show()
  }

  override fun onError(error: FacebookException?) {
    onLoginError(error!!)
  }

  override fun onSuccess(result: LoginResult?) {
    viewPresenter.loginWithFacebook(result?.accessToken!!.token.orEmpty())
  }

  override fun onCancel() {
  }
}
