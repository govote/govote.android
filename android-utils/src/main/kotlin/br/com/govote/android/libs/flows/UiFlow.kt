package br.com.govote.android.libs.flows

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

abstract class UiFlow<A : LifecycleOwner> constructor(private var screen: A?) : LifecycleObserver {

  protected fun getContext(): A = screen!!

  @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
  fun clearContext() {
    screen = null
  }
}
