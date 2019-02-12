package br.com.govote.android

import android.app.Activity
import java.lang.ref.WeakReference

object CurrentActivityHolder {
  private lateinit var currentActivity: WeakReference<Activity?>

  fun setCurrentActivity(activity: Activity) {
    currentActivity = WeakReference(activity)
  }

  fun unsetCurrentActivity() {
    currentActivity = WeakReference(null)
  }

  fun get() = currentActivity.get()
}
