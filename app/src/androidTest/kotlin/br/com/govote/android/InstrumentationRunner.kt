package br.com.govote.android

import android.app.Application
import android.app.Instrumentation
import android.content.Context
import android.os.Bundle
import androidx.test.runner.AndroidJUnitRunner
import com.linkedin.android.testbutler.TestButler

class InstrumentationRunner : AndroidJUnitRunner() {
  @Throws(
    InstantiationException::class,
    IllegalAccessException::class,
    ClassNotFoundException::class
  )
  override fun newApplication(cl: ClassLoader, className: String, context: Context): Application {
    return Instrumentation.newApplication(TestGoVoteApp::class.java, context)
  }

  override fun onStart() {
    TestButler.setup(targetContext)
    super.onStart()
  }

  override fun finish(resultCode: Int, results: Bundle) {
    TestButler.teardown(targetContext)
    super.finish(resultCode, results)
  }
}
