package br.com.govote.android.onboarding

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class OnboardingActivityProvider {

  @ContributesAndroidInjector
  abstract fun contributeOnboardingActivity(): OnboardingActivity
}
