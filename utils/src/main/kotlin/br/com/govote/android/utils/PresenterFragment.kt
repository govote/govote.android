package br.com.govote.android.utils

import android.os.Bundle
import androidx.fragment.app.Fragment
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

abstract class PresenterFragment<V : PresenterView, P : BaseViewPresenter<V>> : Fragment() {

  @Inject lateinit var presenterView: P

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    AndroidSupportInjection.inject(this)
  }

  @Suppress("UNCHECKED_CAST")
  override fun onStart() {
    super.onStart()
    presenterView.bind(this as V)
  }

  override fun onDestroy() {
    super.onDestroy()
    presenterView.release()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    presenterView.release()
  }
}
