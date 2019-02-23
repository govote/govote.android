package br.com.govote.android

import android.app.Activity
import android.app.Application
import android.os.Bundle

class CurrentActivityLifeCycleCallbacks : Application.ActivityLifecycleCallbacks {
  override fun onActivityPaused(activity: Activity?) =
    CurrentActivityHolder.unsetCurrentActivity()

  override fun onActivityResumed(activity: Activity?) =
    CurrentActivityHolder.setCurrentActivity(activity!!)

  override fun onActivityStarted(activity: Activity?) =
    CurrentActivityHolder.setCurrentActivity(activity!!)

  override fun onActivityDestroyed(activity: Activity?) =
    CurrentActivityHolder.unsetCurrentActivity()

  override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
  }

  override fun onActivityStopped(activity: Activity?) = CurrentActivityHolder.unsetCurrentActivity()

  override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {}
}
