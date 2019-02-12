package br.com.govote.android.utils

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel : ViewModel() {

  protected val compositeDisposable: CompositeDisposable = CompositeDisposable()
  protected val loading = MutableLiveData<Boolean>()

  override fun onCleared() {
    super.onCleared()
    clearSubscriptions()
  }

  protected fun clearSubscriptions() {
    compositeDisposable.clear()
  }
}
