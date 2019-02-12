package br.com.govote.android.coordinator.authentication

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.navigation.Navigation
import br.com.govote.android.R
import br.com.govote.android.authentication.LoginNavigation

class LoginNavigator : LoginNavigation {
  override fun onSuccessfulLogin(context: Context) {
    Toast.makeText(context, "OI MUNDO", Toast.LENGTH_LONG).show()
    Navigation.findNavController(context as Activity, R.id.navHost).navigateUp()
  }
}
