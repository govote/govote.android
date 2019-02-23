package br.com.govote.android.about

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import br.com.govote.android.libs.utils.AppUtils
import br.com.govote.android.libs.delegates.viewIdentifiedBy
import br.com.govote.android.libs.extensions.setToolbar
import de.psdev.licensesdialog.LicensesDialog

class AboutActivity : AppCompatActivity() {
  companion object {
    fun newIntent(context: Context): Intent {
      return Intent(context, AboutActivity::class.java)
    }
  }

  private val toolbar: Toolbar by viewIdentifiedBy(R.id.toolbar)
  private val appVersion: TextView by viewIdentifiedBy(R.id.appVersion)
  private val viewLicenses: TextView by viewIdentifiedBy(R.id.viewLicenses)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.about_activity)

    setToolbar(toolbar, R.string.about_title)

    appVersion.text = AppUtils.getVersionName(applicationContext)

    viewLicenses.setOnClickListener {
      LicensesDialog.Builder(this)
        .setNotices(R.raw.notices)
        .setIncludeOwnLicense(true)
        .setTitle(R.string.licenses)
        .setCloseText(R.string.close)
        .build()
        .show()
    }
  }

  override fun onBackPressed() {
    super.onBackPressed()
    finish()
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    if (item.itemId == android.R.id.home) {
      finish()
      return true
    }

    return super.onOptionsItemSelected(item)
  }
}
