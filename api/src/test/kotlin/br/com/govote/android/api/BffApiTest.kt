package br.com.govote.android.api

import br.com.concretesolutions.requestmatcher.LocalTestRequestMatcherRule
import br.com.concretesolutions.requestmatcher.RequestMatcherRule
import br.com.govote.android.api.bff.BffApi
import br.com.govote.android.api.bff.BffApiFactory
import br.com.govote.android.api.bff.BffConfig
import br.com.govote.android.api.bff.dtos.LoginRequest
import br.com.govote.android.api.bff.dtos.LoginResponse
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertNotNull
import java.io.File
import java.io.IOException

class BffApiTest {
  @Rule
  @JvmField
  val serverRule: RequestMatcherRule = LocalTestRequestMatcherRule()
  private var bffApi: BffApi? = null

  @Test
  @Throws(IOException::class)
  fun `ensure api factory configurations`() {
    initServices()
  }

  @Test
  @Throws(IOException::class)
  fun `it should be able to call the remote api as its configuration`() {
    initServices()

    serverRule
      .addResponse(
        MockResponse().setBody(
          Gson().toJson(
            LoginResponse("id"))))
      .ifRequestMatches()

    val response = bffApi!!
      .authenticate(LoginRequest("facebook_id", "facebook_access_token"))
      .blockingFirst()

    assertNotNull(response)
  }

  @Throws(IOException::class)
  private fun initServices() {
    val rootUrl = serverRule.url("/").toString()
    val folder = File.createTempFile("tmp", ".tmp")
    bffApi =
      BffApiFactory.build(OkHttpClient.Builder(), Gson(), BffConfig(rootUrl, folder, "cache", 1024))
  }
}
