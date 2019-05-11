package br.com.govote.android

import androidx.test.filters.SmallTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import br.com.govote.android.main.MainActivity
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class MainActivityTest {
  @get:Rule
  val activityTestRule: ActivityTestRule<MainActivity> = ActivityTestRule(
    MainActivity::class.java
  )

  @Test
  fun ensureHomeInitialization() {
    assertFalse(activityTestRule.activity.isFinishing)
    activityTestRule.finishActivity()
  }
}
