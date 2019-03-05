package br.com.govote.android.onboarding

import br.com.govote.android.libs.ux.UX
import br.com.govote.android.main.MainActivity
import javax.inject.Inject

class OnboardingNavigator @Inject constructor(activity: OnboardingActivity?) :
  UX<OnboardingActivity>(activity) {

  fun toMainScreen() {
    getContext().startActivity(MainActivity.newIntent(getContext()))
    getContext().finish()
  }
}
