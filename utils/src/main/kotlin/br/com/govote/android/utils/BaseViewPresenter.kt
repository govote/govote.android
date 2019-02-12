package br.com.govote.android.utils

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewPresenter<V : PresenterView> protected constructor(protected var presenterView: V?) {

  private val compositeDisposable: CompositeDisposable = CompositeDisposable()

  fun bind(presenterView: V?) = { this.presenterView = presenterView }

  fun release() {
    clearSubscriptions()
    presenterView = null
  }

  fun getView(): V {
    if (presenterView == null) {
      throw IllegalAccessError("presenterView is null and cannot be accessed")
    }

    return presenterView as V
  }

  protected fun addSubscription(disposable: Disposable) = compositeDisposable.add(disposable)

  protected fun clearSubscriptions() = compositeDisposable.clear()
}
