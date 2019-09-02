package br.com.govote.android.data

import androidx.annotation.WorkerThread

abstract class OfflineFirst<A, R> {

  @WorkerThread
  protected abstract fun saveNetworkResult(result: R)
}
