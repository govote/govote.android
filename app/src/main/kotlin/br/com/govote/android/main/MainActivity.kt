package br.com.govote.android.main

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import br.com.govote.android.R
import br.com.govote.android.data.users.UserRepository
import br.com.govote.android.libs.utils.AppUtils
import com.google.android.material.tabs.TabLayout
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {

  private var currentTab = 0

  @Inject lateinit var userRepository: UserRepository
  @Inject lateinit var doubleBackExitBehaviour: DoubleBackExitBehaviour
  @Inject lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

  companion object {
    private const val REQUEST_CODE_RECOVER_PLAY_SERVICES = 9001
    private const val REQUEST_CODE_RECOVER_PLAY_SERVICES_WITHOUT_CALLBACK = 9002
    private const val REQUEST_CODE_WRITE_STORAGE_PERMISSION = 9003

    fun newIntent(context: Context): Intent {
      val intent = Intent(context, MainActivity::class.java)
      intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
      return intent
    }
  }

  // region Activity Events

  override fun onCreate(savedInstanceState: Bundle?) {
    AndroidInjection.inject(this)
    super.onCreate(savedInstanceState)

    if (!AppUtils.checkPlayServices(
        this,
        REQUEST_CODE_RECOVER_PLAY_SERVICES
      )
    ) {
      finish()
      return
    }

    setLayout()

    if (!userRepository.isAuthenticated()) {
      findNavController(R.id.navHost).navigate(R.id.login)
    }
  }

  override fun onSupportNavigateUp() = findNavController(R.id.navHost).navigateUp()

  override fun onBackPressed() {
    doubleBackExitBehaviour.exitOnDoubleBack()
    super.onBackPressed()
  }

  override fun supportFragmentInjector(): AndroidInjector<Fragment> = dispatchingAndroidInjector

  // endregion

  // region Helpers

  private fun setTabIcon(tab: TabLayout.Tab?, @DrawableRes icon: Int) {
    if (tab == null) {
      return
    }

    tab.setIcon(icon)
  }

  private fun setTabColor(tab: TabLayout.Tab?, color: Int) {
    tab?.icon?.setColorFilter(color, PorterDuff.Mode.SRC_IN)
  }

  private fun setLayout() {
    setTheme(R.style.AppTheme)
    setContentView(R.layout.main_activity)
  }

  // endregion
}
