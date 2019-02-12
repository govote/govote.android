package br.com.govote.android.data

import androidx.annotation.WorkerThread
import br.com.govote.android.api.bff.BffApi
import br.com.govote.android.api.bff.dtos.LoginRequest
import br.com.govote.android.api.graphapi.GraphApi
import br.com.govote.android.api.graphapi.utils.FbConsts.Companion.FACEBOOK_USER_FIELDS
import br.com.govote.android.api.graphapi.utils.FbUtils
import io.reactivex.Observable
import javax.inject.Inject

class AuthenticationRepository @Inject constructor(
  private val graphApi: GraphApi,
  private val bffApi: BffApi
) {

  @WorkerThread
  fun authenticate(loginArgs: LoginArgs): Observable<LoginResult> =
    graphApi.me(loginArgs.facebookAccessToken, FbUtils.convert(FACEBOOK_USER_FIELDS.toTypedArray()))
      .toObservable()
      .flatMap { bffApi.authenticate(LoginRequest(it.id, loginArgs.facebookAccessToken)) }
      .map { LoginResult(it.body!!.id) }
}
