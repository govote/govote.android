package br.com.govote.android.onboarding

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import br.com.govote.android.R
import br.com.govote.android.libs.delegates.viewIdentifiedBy
import br.com.govote.android.libs.utils.PagerUtils.createPager
import br.com.govote.android.libs.utils.PagerUtils.markPage
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class OnboardingActivity : AppCompatActivity() {

  private val onboardingNavigator: OnboardingNavigator by inject { parametersOf(this@OnboardingActivity) }

  private val next: TextView by viewIdentifiedBy(R.id.next)
  private val skip: TextView by viewIdentifiedBy(R.id.skip)
  private val steps: LinearLayout by viewIdentifiedBy(R.id.steps)
  private val pageSelected = R.drawable.page_selected
  private val pageUnselected = R.drawable.page_unselected

  private lateinit var navController: NavController
  private val pagination = arrayOfNulls<ImageView>(MAX_ONBOARDING_STEPS)

  companion object {
    private const val MAX_ONBOARDING_STEPS = 2

    fun newIntent(context: Context): Intent =
      with(Intent(context, OnboardingActivity::class.java)) {
        this.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        return this
      }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.onboarding)

    navController = findNavController(this, R.id.navHost)

    createPager(this, pagination, steps, pageSelected, pageUnselected)
    setupNextAction()
    navController.navigate(R.id.welcome)
  }

  internal fun changeStep(step: Int) =
    markPage(this, step, pagination, pageSelected, pageUnselected)

  private fun setupNextAction() =
    next.setOnClickListener {
      when (navController.currentDestination?.id) {
        R.id.welcome -> navController.navigate(R.id.toChooseDeputyAndParty)
        R.id.onboardingDone -> onboardingNavigator.toMainScreen()
      }
    }
}
