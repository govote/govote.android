package br.com.govote.android.api.bff

import br.com.govote.android.api.ApiResponse
import br.com.govote.android.api.bff.dtos.LoginRequest
import br.com.govote.android.api.bff.dtos.LoginResponse
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface BffApi {
  @POST("authentication")
  fun authenticate(@Body loginRequest: LoginRequest): Observable<ApiResponse<LoginResponse>>

  @GET("projects/ids")
  fun projectIdentifiers(): Observable<ApiResponse<List<String>>>
}
