package br.com.govote.android.data

import android.content.SharedPreferences
import javax.inject.Inject

class UserRepository @Inject constructor(private val sharedPreferences: SharedPreferences) {

  fun isAuthenticated(): Boolean = sharedPreferences.getBoolean(Companion.AUTH_KEY, false)

  fun authenticate() = sharedPreferences.edit().putBoolean(AUTH_KEY, true)

  companion object {
    private const val AUTH_KEY = "auth"
  }
}
