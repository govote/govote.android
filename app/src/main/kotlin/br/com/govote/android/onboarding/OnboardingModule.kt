package br.com.govote.android.onboarding

import dagger.Module
import dagger.Provides

@Module
class OnboardingModule {

  @Provides
  fun provideOnboardingNavigation(onboardingActivity: OnboardingActivity): OnboardingNavigator =
    OnboardingNavigator(onboardingActivity)
}
