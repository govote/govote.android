package br.com.govote.android.analytics

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.google.firebase.analytics.FirebaseAnalytics

class AnalyticsInitializerContentProvider : ContentProvider() {

  override fun insert(uri: Uri, values: ContentValues?): Uri? = null

  override fun query(
    uri: Uri,
    projection: Array<String>?,
    selection: String?,
    selectionArgs: Array<String>?,
    sortOrder: String?
  ): Cursor? = null

  override fun onCreate(): Boolean {
    val applicationContext = context?.applicationContext
    val isEnabled = !BuildConfig.DEBUG

    FirebaseAnalytics.getInstance(applicationContext!!).setAnalyticsCollectionEnabled(isEnabled)

    return true
  }

  override fun update(
    uri: Uri,
    values: ContentValues?,
    selection: String?,
    selectionArgs: Array<String>?
  ): Int = 0

  override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int = 0

  override fun getType(uri: Uri): String? = null
}
