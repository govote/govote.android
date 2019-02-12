package br.com.govote.android.api.bff

import java.io.File

data class Config(
  internal val uri: String,
  internal val cacheDir: File,
  internal val cacheName: String,
  internal val cacheSize: Int
)
