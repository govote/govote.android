package br.com.govote.android.data.onboarding

import android.content.SharedPreferences
import javax.inject.Inject

class OnboardingRepository @Inject constructor(private val sharedPreferences: SharedPreferences) {

  companion object {
    private const val ONBOARDING_KEY = "onboarding.show"
  }

  fun showOnboarding() = sharedPreferences.getBoolean(ONBOARDING_KEY, true)

  fun markAsViewed() = sharedPreferences.edit().putBoolean(ONBOARDING_KEY, false)
}
