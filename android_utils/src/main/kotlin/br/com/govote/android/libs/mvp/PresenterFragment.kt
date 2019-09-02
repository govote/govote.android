package br.com.govote.android.libs.mvp

import androidx.fragment.app.Fragment

abstract class PresenterFragment<V : PresenterView, P : BaseViewPresenter<V>> : Fragment() {

  lateinit var viewPresenter: P

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
