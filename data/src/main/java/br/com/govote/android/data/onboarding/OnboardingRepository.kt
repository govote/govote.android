package br.com.govote.android.data.onboarding

import android.content.SharedPreferences

class OnboardingRepository constructor(private val sharedPreferences: SharedPreferences) {

  companion object {
    private const val ONBOARDING_KEY = "onboarding.viewed"
  }

  fun showOnboarding(): Boolean {
    val isShowing = sharedPreferences.getBoolean(ONBOARDING_KEY, false)

    if (isShowing) {
      markAsViewed()
    }

    return isShowing
  }

  private fun markAsViewed() = sharedPreferences.edit().putBoolean(ONBOARDING_KEY, true).apply()
}
