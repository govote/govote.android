package br.com.govote.android.analytics

import android.content.Context

interface AnalyticsAdapter {

  fun trackAction(context: Context, args: TraceActionArgs)

  fun trackView(context: Context, args: TraceScreenArgs)
}
