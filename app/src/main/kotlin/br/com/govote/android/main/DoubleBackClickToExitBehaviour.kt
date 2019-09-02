package br.com.govote.android.main

import android.os.Handler
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import br.com.govote.android.R

class DoubleBackClickToExitBehaviour constructor(private var activity: MainActivity?) :
  LifecycleObserver {

  private var doubleBackToExitPressedOnce = false
  private val doubleBackHandler = Handler()
  private val exitRunnable = { doubleBackToExitPressedOnce = false }

  fun exitOnDoubleBack() {
    var exitToast: Toast? = null

    if (doubleBackToExitPressedOnce) {
      exitToast?.cancel()
      activity?.finish()

      return
    }

    doubleBackToExitPressedOnce = true
    exitToast = Toast.makeText(activity, R.string.double_back_to_exit, Toast.LENGTH_SHORT)

    exitToast!!.show()
    doubleBackHandler.postDelayed(exitRunnable, DELAY_TO_EXIT)
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
  fun onDestroy() {
    doubleBackHandler.removeCallbacks(exitRunnable)
    activity = null
  }

  companion object {
    private const val DELAY_TO_EXIT = 2000L
  }
}
