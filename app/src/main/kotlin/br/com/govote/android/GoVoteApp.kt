package br.com.govote.android

import android.app.Activity
import android.app.Application
import android.content.ComponentCallbacks2
import br.com.govote.android.config.FrescoPipelines
import br.com.govote.android.config.RemoteConfig
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.cache.ImageCacheStatsTracker
import com.google.firebase.FirebaseApp
import com.squareup.leakcanary.RefWatcher
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

open class GoVoteApp : Application(), HasActivityInjector {

  companion object {
    const val SHARED_PREFERENCES = "br.com.govote.android.shared_prefs"
    const val DATABASE = "br.com.govote.android.database"

    lateinit var instance: GoVoteApp private set
    lateinit var refWatcher: RefWatcher private set
  }

  @Inject lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>
  @Inject lateinit var imageCacheStatsTracker: ImageCacheStatsTracker

  override fun onCreate() {
    super.onCreate()

    setupDagger()
    FrescoPipelines.setup(this, imageCacheStatsTracker)
    FirebaseApp.initializeApp(this)
    RemoteConfig.setup()

    registerActivityLifecycleCallbacks(CurrentActivityLifeCycleCallbacks())

    refWatcher = enableLeakCanary()
    instance = this
  }

  override fun onTrimMemory(level: Int) {
    super.onTrimMemory(level)

    when (level) {
      ComponentCallbacks2.TRIM_MEMORY_COMPLETE,
      ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW,
      ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL,
      ComponentCallbacks2.TRIM_MEMORY_BACKGROUND,
      ComponentCallbacks2.TRIM_MEMORY_MODERATE,
      ComponentCallbacks2.TRIM_MEMORY_RUNNING_MODERATE,
      ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN ->
        if (Fresco.hasBeenInitialized()) Fresco.getImagePipeline().clearMemoryCaches()
    }
  }

  override fun activityInjector(): AndroidInjector<Activity>? = dispatchingAndroidInjector

  private fun setupDagger() =
    DaggerAppComponent
      .builder()
      .application(this)
      .build()
      .inject(this)

  protected open fun enableLeakCanary(): RefWatcher = RefWatcher.DISABLED
}
