package br.com.govote.android.main

import android.graphics.PorterDuff
import androidx.annotation.DrawableRes
import br.com.govote.android.libs.flows.UiFlow
import com.google.android.material.tabs.TabLayout

class OnTabChangeBehaviour constructor(mainActivity: MainActivity) :
  UiFlow<MainActivity>(mainActivity), TabLayout.OnTabSelectedListener {

  private fun setTabIcon(tab: TabLayout.Tab?, @DrawableRes icon: Int) = tab?.setIcon(icon)

  private fun setTabColor(tab: TabLayout.Tab?, color: Int) =
    tab?.icon?.setColorFilter(color, PorterDuff.Mode.SRC_IN)

  override fun onTabReselected(p0: TabLayout.Tab?) {
  }

  override fun onTabUnselected(p0: TabLayout.Tab?) {
  }

  override fun onTabSelected(p0: TabLayout.Tab?) {
  }

}
