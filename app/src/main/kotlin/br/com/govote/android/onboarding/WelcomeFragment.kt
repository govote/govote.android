package br.com.govote.android.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.govote.android.R

class WelcomeFragment : OnboardingFragment() {

  override fun getStep(): Int = 0

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? = inflater.inflate(R.layout.onboarding_welcome, container, false)
}
