package br.com.govote.android.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import br.com.govote.android.R
import br.com.govote.android.auth.LoginActivity
import br.com.govote.android.libs.delegates.viewIdentifiedBy
import br.com.govote.android.libs.mvp.PresenterActivity
import br.com.govote.android.libs.ui.ViewPagerAdapter
import br.com.govote.android.libs.utils.AppUtils.checkPlayServices
import br.com.govote.android.main.RequestCodes.RECOVER_PLAY_SERVICES
import br.com.govote.android.voting.VotingFragment
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import java.util.*

class MainActivity : PresenterActivity<MainView, MainPresenter>(), MainView {

  private val doubleBackClickToExitBehaviour: DoubleBackClickToExitBehaviour by inject { parametersOf(this@MainActivity) }
  private val pager: ViewPager by viewIdentifiedBy(R.id.pager)
  override val viewPresenter: MainPresenter by inject { parametersOf(this@MainActivity) }

  companion object {
    fun newIntent(context: Context): Intent =
      with(Intent(context, MainActivity::class.java)) {
        this.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        return this
      }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    if (!checkPlayServices(this, RECOVER_PLAY_SERVICES)) {
      finish()
      return
    }

    setTheme(R.style.AppTheme)
    setContentView(R.layout.main_activity)

    viewPresenter.startMainFlowValidations()
  }

  override fun onBackPressed() {
    doubleBackClickToExitBehaviour.exitOnDoubleBack()
    super.onBackPressed()
  }

  override fun onAuthenticationNeeded() {
    startActivity(LoginActivity.newClearIntent(this))
    finish()
  }

  override fun onAuthenticated() {
    val fragments = Vector<Fragment>()
    val fragmentFactory = supportFragmentManager.fragmentFactory

    fragments.add(fragmentFactory.instantiate(classLoader, HomeFragment::class.java.name, null))
    fragments.add(fragmentFactory.instantiate(classLoader, VotingFragment::class.java.name, null))

    pager.adapter = ViewPagerAdapter(supportFragmentManager, fragments)
  }

  override fun showOnboarding() = MainNavigator.toOnboarding(this)
}
