package br.com.govote.android.libs.mvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel : ViewModel() {

  protected val compositeDisposable: CompositeDisposable = CompositeDisposable()
  protected val loading = MutableLiveData<Boolean>()

  override fun onCleared() {
    super.onCleared()
    clearSubscriptions()
  }

  protected fun addSubscription(disposable: Disposable) = compositeDisposable.add(disposable)

  protected fun clearSubscriptions() = compositeDisposable.clear()
}
