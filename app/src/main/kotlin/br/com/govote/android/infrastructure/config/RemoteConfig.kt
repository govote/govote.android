package br.com.govote.android.infrastructure.config

import br.com.govote.android.BuildConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

object RemoteConfig {
  fun setup() =
    FirebaseRemoteConfig
      .getInstance()
      .setConfigSettings(
        FirebaseRemoteConfigSettings.Builder()
          .setDeveloperModeEnabled(BuildConfig.DEBUG)
          .build()
      )
}
