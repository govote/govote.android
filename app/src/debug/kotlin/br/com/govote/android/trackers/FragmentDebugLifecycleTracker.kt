package br.com.govote.android.trackers

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import br.com.govote.android.libs.logger.LogUtility

class FragmentDebugLifecycleTracker : FragmentManager.FragmentLifecycleCallbacks() {
  override fun onFragmentActivityCreated(fm: FragmentManager, f: Fragment,
                                         savedInstanceState: Bundle?) =
    LogUtility.d("Fragment CREATED --> " + f.javaClass.simpleName)

  override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View,
                                     savedInstanceState: Bundle?) =
    LogUtility.d("Fragment VIEW CREATED --> " + f.javaClass.simpleName)

  override fun onFragmentStarted(fm: FragmentManager, f: Fragment) =
    LogUtility.d("Fragment STARTED --> " + f.javaClass.simpleName)

  override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) =
    LogUtility.d("Fragment ATTACHED --> " + f.javaClass.simpleName)

  override fun onFragmentResumed(fm: FragmentManager, f: Fragment) =
    LogUtility.d("Fragment RESUMED --> " + f.javaClass.simpleName)

  override fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle) =
    LogUtility.d("Fragment SAVED INSTANCE STATE --> " + f.javaClass.simpleName)

  override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) =
    LogUtility.d("Fragment DESTROYED --> " + f.javaClass.simpleName)

  override fun onFragmentPaused(fm: FragmentManager, f: Fragment) =
    LogUtility.d("Fragment PAUSED --> " + f.javaClass.simpleName)

  override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) =
    LogUtility.d("Fragment VIEW DESTROYED --> " + f.javaClass.simpleName)
}
