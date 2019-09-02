package br.com.govote.android.libs.flows

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

abstract class UiFlow<T : LifecycleOwner> constructor(private var screen: T?) : LifecycleObserver {

  protected fun getUiContext(): T = screen!!

  @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
  fun clearUiContext() {
    screen = null
  }
}
