package br.com.govote.android.analytics

data class TraceScreenArgs @JvmOverloads constructor(
    val screen: String,
    val params: Map<String, Any> = emptyMap()
)
