package br.com.govote.android.coordinator.home

import androidx.test.filters.SmallTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import br.com.govote.android.MainActivity
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class MainAnalyticsTest {
  @get:Rule
  val mActivityTestRule: ActivityTestRule<MainActivity> = ActivityTestRule(
    MainActivity::class.java)

  @Test
  fun ensureHomeInitialization() {
    assertFalse(mActivityTestRule.activity.isFinishing)
    mActivityTestRule.finishActivity()
  }
}
