package br.com.govote.android

import br.com.govote.android.trackers.FrescoDebugCacheStatsTracker
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

val perBuildModule = module {
  single { okHttpBuilder() }
  single(named("frescoDebug")) { FrescoDebugCacheStatsTracker() }
}

private fun okHttpBuilder(): OkHttpClient.Builder =
  OkHttpClient.Builder()
    .addNetworkInterceptor(StethoInterceptor())
    .addInterceptor(HttpLoggingInterceptor().setLevel(BODY))
    .connectTimeout(15, TimeUnit.SECONDS)
    .readTimeout(20, TimeUnit.SECONDS)
    .writeTimeout(15, TimeUnit.SECONDS)
