package br.com.govote.android.data.authentication

import android.content.SharedPreferences
import androidx.annotation.WorkerThread
import br.com.govote.android.api.bff.BffApi
import br.com.govote.android.api.bff.dtos.LoginRequest
import br.com.govote.android.api.graphapi.GraphApi
import br.com.govote.android.api.graphapi.utils.FbConsts.Companion.FACEBOOK_USER_FIELDS
import br.com.govote.android.api.graphapi.utils.FbUtils
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import io.reactivex.Observable

class AuthenticationRepository constructor(
  private val graphApi: GraphApi,
  private val bffApi: BffApi,
  private val sharedPreferences: SharedPreferences
) {

  fun isAuthenticated(): Boolean = sharedPreferences.getBoolean(AUTH_KEY, false)

  @WorkerThread
  fun authenticate(loginArgs: LoginArgs): Observable<LoginResult> =
    graphApi.me(loginArgs.facebookAccessToken, FbUtils.convert(FACEBOOK_USER_FIELDS.toTypedArray()))
      .toObservable()
      .flatMap { bffApi.authenticate(LoginRequest(it.id, loginArgs.facebookAccessToken)) }
      .doOnNext { sharedPreferences.edit().putBoolean(AUTH_KEY, true).apply() }
      .map { LoginResult(it.body!!.id) }

  companion object {
    private const val AUTH_KEY = "auth"
  }
}
