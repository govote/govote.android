package br.com.govote.android

import android.net.TrafficStats
import android.os.StrictMode
import br.com.govote.android.libs.logger.LogUtility
import br.com.govote.android.trackers.ActivityDebugLifecycleTracker
import br.com.govote.android.trackers.FragmentDebugLifecycleTracker
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.android.utils.FlipperUtils
import com.facebook.flipper.plugins.crashreporter.CrashReporterPlugin
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.flipper.plugins.leakcanary.LeakCanaryFlipperPlugin
import com.facebook.flipper.plugins.leakcanary.RecordLeakService
import com.facebook.soloader.SoLoader
import com.facebook.stetho.Stetho
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import com.tspoon.traceur.Traceur
import timber.log.Timber

class DebugGoVoteApp : GoVoteApp() {
  override fun onCreate() {
    if (LeakCanary.isInAnalyzerProcess(this)) {
      return
    }

    super.onCreate()

    SoLoader.init(this, false)

    if (FlipperUtils.shouldEnableFlipper(this)) {
      val client = AndroidFlipperClient.getInstance(this)

      client.addPlugin(InspectorFlipperPlugin(this, DescriptorMapping.withDefaults()))
      client.addPlugin(LeakCanaryFlipperPlugin())
      client.addPlugin(CrashReporterPlugin.getInstance())

      client.start()
    }

    Stetho.initialize(
      Stetho.newInitializerBuilder(this)
        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
        .build()
    )

    trackActivitiesAndFragmentsLifecycle()

    Traceur.enableLogging()
    Timber.plant(Timber.DebugTree())

    // strict mode should be enabled at to end to avoid errors caused by certain libraries initialization
    enableStrictMode()
  }

  override fun onTrimMemory(level: Int) {
    super.onTrimMemory(level)
    LogUtility.d("[ onTrimMemory ] $level")
  }

  override fun enableLeakCanary(): RefWatcher =
    LeakCanary.refWatcher(this)
      .listenerServiceClass(RecordLeakService::class.java)
      .buildAndInstall()

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
