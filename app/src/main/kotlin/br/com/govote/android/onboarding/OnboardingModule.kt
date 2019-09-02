package br.com.govote.android.onboarding

import org.koin.dsl.module

val onboardingModule = module {
  factory { (activity: OnboardingActivity) -> provideOnboardingNavigation(activity) }
}

fun provideOnboardingNavigation(onboardingActivity: OnboardingActivity): OnboardingNavigator =
  OnboardingNavigator(onboardingActivity)
