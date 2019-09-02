package br.com.govote.android.onboarding

import br.com.govote.android.libs.flows.UiFlow
import br.com.govote.android.main.MainActivity

class OnboardingNavigator constructor(activity: OnboardingActivity?) :
  UiFlow<OnboardingActivity>(activity) {

  fun toMainScreen() {
    getUiContext().startActivity(MainActivity.newIntent(getUiContext()))
    getUiContext().finish()
  }
}
