package br.com.govote.android.main

import br.com.govote.android.data.authentication.AuthenticationRepository
import br.com.govote.android.data.onboarding.OnboardingRepository
import br.com.govote.android.libs.mvp.BaseViewPresenter
import javax.inject.Inject

class MainPresenter @Inject constructor(
  mainView: MainView,
  private val authenticationRepository: AuthenticationRepository,
  private val onboardingRepository: OnboardingRepository)
  : BaseViewPresenter<MainView>(mainView) {

  fun startMainFlowValidations() {
    if (authenticationRepository.isAuthenticated()) {
      if (onboardingRepository.showOnboarding()) {
        getView().showOnboard()
        return
      }

      getView().onAuthenticated()
      return
    }

    getView().onAuthenticationNeeded()
  }
}
