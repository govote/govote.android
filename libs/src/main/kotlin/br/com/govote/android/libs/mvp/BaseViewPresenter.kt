package br.com.govote.android.libs.mvp

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewPresenter<V : PresenterView> protected constructor(private var presenterView: V?) {

  private val compositeDisposable: CompositeDisposable = CompositeDisposable()

  fun bind(presenterView: V?) = { this.presenterView = presenterView }

  fun releaseView() {
    presenterView = null
  }

  fun clear() {
    releaseView()
    clearSubscriptions()
  }

  fun getView(): V {
    if (presenterView == null) {
      throw IllegalAccessError("viewPresenter is null and cannot be accessed")
    }

    return presenterView as V
  }

  protected fun addSubscription(disposable: Disposable) = compositeDisposable.add(disposable)

  protected fun clearSubscriptions() = compositeDisposable.clear()
}
