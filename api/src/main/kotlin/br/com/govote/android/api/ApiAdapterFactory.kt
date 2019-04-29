package br.com.govote.android.api

import io.reactivex.Observable
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ApiAdapterFactory : CallAdapter.Factory() {
  override fun get(returnType: Type, annotations: Array<Annotation>,
                   retrofit: Retrofit): CallAdapter<*, *>? {
    if (getRawType(returnType) != Observable::class.java) {
      return null
    }

    val observableType = getParameterUpperBound(0, returnType as ParameterizedType)
    val rawObservableType = getRawType(observableType)

    if (rawObservableType != ApiResponse::class.java) {
      throw IllegalArgumentException("type must be a resource")
    }

    if (observableType !is ParameterizedType) {
      throw IllegalArgumentException("resource must be parametrized")
    }

    val bodyType = getParameterUpperBound(0, observableType)

    return ApiCallAdapter<Any>(bodyType)
  }
}
