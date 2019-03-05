package br.com.govote.android.onboarding

import androidx.fragment.app.Fragment

abstract class OnboardingFragment : Fragment() {

  abstract fun getStep(): Int

  override fun onStart() {
    super.onStart()
    (activity as OnboardingActivity)
      .changeStep(getStep())
  }
}
