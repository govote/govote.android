package br.com.govote.android.analytics

import android.app.Activity

interface AnalyticsActivityProvider {

  fun currentActivity(): Activity
}
