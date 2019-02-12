package br.com.govote.android

import com.squareup.leakcanary.RefWatcher

class TestGoVoteApp : GoVoteApp() {
  override fun enableLeakCanary(): RefWatcher {
    return RefWatcher.DISABLED
  }
}
