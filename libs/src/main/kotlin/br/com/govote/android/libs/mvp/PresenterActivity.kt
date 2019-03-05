package br.com.govote.android.libs.mvp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.android.AndroidInjection
import javax.inject.Inject

abstract class PresenterActivity<V : PresenterView, P : BaseViewPresenter<V>> : AppCompatActivity() {

  @Inject lateinit var viewPresenter: P

  @Suppress("UNCHECKED_CAST")
  override fun onCreate(savedInstanceState: Bundle?) {
    AndroidInjection.inject(this)
    super.onCreate(savedInstanceState)
    viewPresenter.bind(this as V)
  }

  override fun onDestroy() {
    super.onDestroy()
    viewPresenter.clear()
    viewPresenter.releaseView()
  }
}
