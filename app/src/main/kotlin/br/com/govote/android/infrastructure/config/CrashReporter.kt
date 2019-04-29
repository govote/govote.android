package br.com.govote.android.infrastructure.config

import android.content.Context
import br.com.govote.android.BuildConfig
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import io.fabric.sdk.android.Fabric

object CrashReporter {
  fun setup(context: Context): Fabric =
    Fabric.with(
      context, Crashlytics.Builder()
      .core(CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
      .build())
}
