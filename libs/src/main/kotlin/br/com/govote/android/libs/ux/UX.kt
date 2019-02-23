package br.com.govote.android.libs.ux

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

open class UX<A : LifecycleOwner> constructor(private var screen: A?) : LifecycleObserver {

  protected fun getScreen() = screen

  @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
  fun clearScreen() {
    screen = null
  }
}
