package br.com.govote.android.onboarding

import br.com.govote.android.libs.flows.UiFlow
import br.com.govote.android.main.MainActivity
import javax.inject.Inject

class OnboardingNavigator @Inject constructor(activity: OnboardingActivity?) :
  UiFlow<OnboardingActivity>(activity) {

  fun toMainScreen() {
    getContext().startActivity(MainActivity.newIntent(getContext()))
    getContext().finish()
  }
}
