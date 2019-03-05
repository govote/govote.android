package br.com.govote.android.main

import br.com.govote.android.onboarding.OnboardingActivity

object MainNavigator {

  fun toOnboarding(mainActivity: MainActivity) {
    mainActivity.startActivity(OnboardingActivity.newIntent(mainActivity))
    mainActivity.finish()
  }
}
