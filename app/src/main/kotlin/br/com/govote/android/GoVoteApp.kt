package br.com.govote.android

import android.app.Application
import android.content.ComponentCallbacks2
import br.com.govote.android.infrastructure.config.CrashReporter
import br.com.govote.android.infrastructure.config.FrescoPipelines
import br.com.govote.android.infrastructure.config.RemoteConfig
import br.com.govote.android.infrastructure.di.appComponents
import br.com.govote.android.libs.callbacks.CurrentActivityLifeCycleCallbacks
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.cache.ImageCacheStatsTracker
import com.google.firebase.FirebaseApp
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.qualifier.named

open class GoVoteApp : Application() {

  companion object {
    const val SHARED_PREFERENCES = "br.com.govote.android.shared_prefs"
    const val DATABASE = "br.com.govote.android.database"

    lateinit var instance: GoVoteApp private set
  }

  override fun onCreate() {
    super.onCreate()

    startKoin {
      androidContext(this@GoVoteApp)
      androidLogger(Level.DEBUG)
      modules(appComponents)
    }

    val imageCacheStatsTracker: ImageCacheStatsTracker = get(named("frescoDebug"))

    FrescoPipelines.setup(this, imageCacheStatsTracker)
    FirebaseApp.initializeApp(this)
    RemoteConfig.setup()
    CrashReporter.setup(this)

    registerActivityLifecycleCallbacks(CurrentActivityLifeCycleCallbacks())

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
}
