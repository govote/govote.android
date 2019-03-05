package br.com.govote.android.main

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import br.com.govote.android.R
import br.com.govote.android.authentication.LoginActivity
import br.com.govote.android.libs.mvp.PresenterActivity
import br.com.govote.android.libs.ui.ViewPagerAdapter
import br.com.govote.android.libs.utils.AppUtils.checkPlayServices
import br.com.govote.android.main.RequestCodes.RECOVER_PLAY_SERVICES
import com.google.android.material.tabs.TabLayout
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class MainActivity : PresenterActivity<MainView, MainPresenter>(), MainView,
                     HasSupportFragmentInjector {

  @Inject lateinit var doubleBackClickToExitBehaviour: DoubleBackClickToExitBehaviour
  @Inject lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>
  private lateinit var tabsAdapter: ViewPagerAdapter

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

  override fun supportFragmentInjector(): AndroidInjector<Fragment> = dispatchingAndroidInjector

  override fun onAuthenticationNeeded() {
    startActivity(LoginActivity.newClearIntent(this))
    finish()
  }

  override fun onAuthenticated() {

  }

  override fun showOnboard() = MainNavigator.toOnboarding(this)
}
