package br.com.govote.android.libs.mvp

import android.os.Bundle
import androidx.fragment.app.Fragment
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

abstract class PresenterFragment<V : PresenterView, P : BaseViewPresenter<V>> : Fragment() {

  @Inject lateinit var viewPresenter: P

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    AndroidSupportInjection.inject(this)
  }

  @Suppress("UNCHECKED_CAST")
  override fun onStart() {
    super.onStart()
    viewPresenter.bind(this as V)
  }

  override fun onDestroy() {
    super.onDestroy()
    viewPresenter.clear()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    viewPresenter.releaseView()
  }
}
