package br.com.govote.android.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.govote.android.R

class DoneFragment : OnboardingFragment() {

  override fun getStep(): Int = 1

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? = inflater.inflate(R.layout.onboarding_done, container, false)
}
