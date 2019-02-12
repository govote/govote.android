package br.com.govote.android.api.graphapi

data class GraphApiConfig(
  internal val uri: String,
  internal val cacheDir: String,
  internal val cacheSize: Int
)
