package br.com.govote.android.trackers

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import br.com.govote.android.libs.logger.LogUtility

class ActivityDebugLifecycleTracker(private val trackerDebug: FragmentDebugLifecycleTracker) :
  Application.ActivityLifecycleCallbacks {

  override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
    LogUtility.d("Activity CREATED --> " + activityName(activity))

    if (activity is AppCompatActivity) {
      activity.supportFragmentManager
        .registerFragmentLifecycleCallbacks(trackerDebug, false)
    }
  }

  override fun onActivityStarted(activity: Activity?) =
    LogUtility.d("Activity STARTED --> " + activityName(activity))

  override fun onActivityResumed(activity: Activity?) =
    LogUtility.d("Activity RESUMED --> " + activityName(activity))

  override fun onActivityPaused(activity: Activity?) =
    LogUtility.d("Activity PAUSED --> " + activityName(activity))

  override fun onActivityStopped(activity: Activity?) =
    LogUtility.d("Activity STOPPED --> " + activityName(activity))

  override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) =
    LogUtility.d("Activity SAVED INSTANCE STATE --> " + activityName(activity))

  override fun onActivityDestroyed(activity: Activity?) {
    LogUtility.d("Activity DESTROYED --> " + activityName(activity))

    if (activity is AppCompatActivity) {
      activity.supportFragmentManager
        .unregisterFragmentLifecycleCallbacks(trackerDebug)
    }
  }

  private fun activityName(activity: Activity?) = activity?.javaClass?.simpleName.orEmpty()
}
