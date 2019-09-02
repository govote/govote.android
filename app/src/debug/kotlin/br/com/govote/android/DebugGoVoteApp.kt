package br.com.govote.android

import android.os.StrictMode
import br.com.govote.android.libs.logger.LogUtility
import br.com.govote.android.trackers.ActivityDebugLifecycleTracker
import br.com.govote.android.trackers.FragmentDebugLifecycleTracker
import com.facebook.stetho.Stetho
import com.tspoon.traceur.Traceur
import timber.log.Timber

class DebugGoVoteApp : GoVoteApp() {
  override fun onCreate() {
    super.onCreate()

    Stetho.initialize(
      Stetho.newInitializerBuilder(this)
        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
        .build()
    )

    trackActivitiesAndFragmentsLifecycle()

    Traceur.enableLogging()
    Timber.plant(Timber.DebugTree())

    enableStrictMode()
  }

  override fun onTrimMemory(level: Int) {
    super.onTrimMemory(level)
    LogUtility.d("[ onTrimMemory ] $level")
  }

  private fun enableStrictMode() {
    StrictMode.setThreadPolicy(
      StrictMode.ThreadPolicy.Builder()
        .detectAll()
        .penaltyFlashScreen()
        .penaltyLog()
        .build()
    )

    StrictMode.setVmPolicy(
      StrictMode.VmPolicy.Builder()
        .detectAll()
        .penaltyLog()
        .build()
    )
  }

  private fun trackActivitiesAndFragmentsLifecycle() =
    registerActivityLifecycleCallbacks(ActivityDebugLifecycleTracker(FragmentDebugLifecycleTracker()))
}
