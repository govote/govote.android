package br.com.govote.android

import br.com.govote.android.trackers.FrescoDebugCacheStatsTracker
import com.facebook.imagepipeline.cache.ImageCacheStatsTracker
import com.facebook.stetho.okhttp3.StethoInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
internal class PerBuildModule {
  
  @Provides
  @Singleton
  fun okHttpBuilder(): OkHttpClient.Builder =
    OkHttpClient.Builder()
      .addNetworkInterceptor(StethoInterceptor())
      .addInterceptor(HttpLoggingInterceptor().setLevel(BODY))
      .connectTimeout(15, TimeUnit.SECONDS)
      .readTimeout(20, TimeUnit.SECONDS)
      .writeTimeout(15, TimeUnit.SECONDS)

  @Provides
  fun imageCacheStatsTracker(): ImageCacheStatsTracker = FrescoDebugCacheStatsTracker()
}
