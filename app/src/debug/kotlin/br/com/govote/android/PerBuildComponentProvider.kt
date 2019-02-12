package br.com.govote.android

import br.com.govote.android.trackers.FrescoDebugCacheStatsTracker
import com.facebook.imagepipeline.cache.ImageCacheStatsTracker
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import java.util.concurrent.TimeUnit

internal class PerBuildComponentProvider {
  fun okHttpBuilder(): OkHttpClient.Builder {
    return OkHttpClient.Builder()
      .addNetworkInterceptor(StethoInterceptor())
      .addInterceptor(HttpLoggingInterceptor().setLevel(BODY))
      .connectTimeout(15, TimeUnit.SECONDS)
      .readTimeout(20, TimeUnit.SECONDS)
      .writeTimeout(15, TimeUnit.SECONDS)
  }

  fun imageCacheStatsTracker(): ImageCacheStatsTracker? {
    return FrescoDebugCacheStatsTracker()
  }

  companion object {
    private var instance: PerBuildComponentProvider? = null

    fun getInstance(): PerBuildComponentProvider {
      if (instance == null) {
        instance = PerBuildComponentProvider()
      }

      return instance as PerBuildComponentProvider
    }
  }
}
