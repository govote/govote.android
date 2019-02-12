package br.com.govote.android.authentication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import br.com.govote.android.utils.PresenterFragment
import br.com.govote.android.utils.delegates.viewWithId

class LoginFragment : LoginView, PresenterFragment<LoginView, LoginPresenter>() {

  private val facebookLogin: Button by viewWithId(R.id.facebookLogin)

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? = inflater.inflate(R.layout.login_fragment, container, false)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    facebookLogin.setOnClickListener { presenterView.loginWithFacebook(this) }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    presenterView.result(requestCode, resultCode, data)
  }

  override fun onSuccessfulLogin() {
    findNavController().popBackStack()
  }

  override fun onError() {
  }
}
