package br.com.govote.android.analytics

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics

class FirebaseAnalyticsAdapter(private val analyticsActivityProvider: AnalyticsActivityProvider) :
  AnalyticsAdapter {

  override fun trackAction(context: Context, args: TraceActionArgs) =
    FirebaseAnalytics.getInstance(context).logEvent(args.action, toBundle(args.params))

  override fun trackView(context: Context, args: TraceScreenArgs) =
    FirebaseAnalytics.getInstance(context)
      .setCurrentScreen(analyticsActivityProvider.currentActivity(), args.screen, null)
}
