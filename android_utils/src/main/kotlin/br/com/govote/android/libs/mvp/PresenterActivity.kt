package br.com.govote.android.libs.mvp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.koin.android.ext.android.inject

abstract class PresenterActivity<V : PresenterView, P : BaseViewPresenter<V>> :
  AppCompatActivity() {

  abstract val viewPresenter: P

  @Suppress("UNCHECKED_CAST")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewPresenter.bind(this as V)
  }

  override fun onDestroy() {
    super.onDestroy()
    viewPresenter.clear()
    viewPresenter.releaseView()
  }
}
