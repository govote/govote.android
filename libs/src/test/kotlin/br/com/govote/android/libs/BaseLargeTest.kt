package br.com.govote.android.libs

import br.com.concretesolutions.requestmatcher.LocalTestRequestMatcherRule
import br.com.concretesolutions.requestmatcher.RequestMatcherRule
import org.junit.After
import org.junit.Before
import org.junit.Rule

abstract class BaseLargeTest {
  @Rule
  val server: RequestMatcherRule = LocalTestRequestMatcherRule()

  @Before
  fun baseSetUp() {
    server.url("/")
  }

  @After
  fun baseTearDown() {
  }
}
