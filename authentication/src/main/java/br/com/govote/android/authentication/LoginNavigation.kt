package br.com.govote.android.authentication

import android.content.Context

interface LoginNavigation {
  fun onSuccessfulLogin(context: Context)
}
