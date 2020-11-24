package tech.camargo.covid

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import org.koin.android.ext.android.inject
import tech.camargo.covid.databinding.ActivityConfigBinding
import tech.camargo.covid.utils.Linter

class ConfigActivity : AppCompatActivity() {

    private lateinit var B: ActivityConfigBinding
    private val persistent: Persistent by inject()
    private val linter: Linter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        B = DataBindingUtil.setContentView(this, R.layout.activity_config)
        setSupportActionBar(B.toolbar)
        title = getString(R.string.settings)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        B.cellphone = persistent.cellphones
        showBuild()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.save -> {
                save()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.config, menu)
        return true
    }

    private fun save() {
        if (linter.lintPhone(B.cellphone)) {
            persistent.cellphones = B.cellphone
            showDone(R.string.settings_saved)
        } else {
            showError(R.string.phone_wrong)
        }
    }

    private fun showBuild() {
        try {
            val version = packageManager.getPackageInfo(packageName, 0).versionName ?: "X"
            B.versionText.text = getString(R.string.version_code, version)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }

    fun resetList(view: View) {
        persistent.resetList()
        showDone(R.string.clear_visits_done)
    }

    fun reset(view: View) {
        persistent.reset()
        showDone(R.string.cleared_all)
    }

    fun list(view: View) {
        ListActivity.start(this)
    }

    fun about(view: View) {
        ReaderActivity.about(this)
    }

    fun privacy(view: View) {
        ReaderActivity.privacy(this)
    }

    fun terms(view: View) {
        ReaderActivity.terms(this)
    }

    private fun done(message: Int) {
        runOnUiThread {
            AlertDialog.Builder(this, R.style.BottomDialogStyle).apply {
                setMessage(message)
                setPositiveButton(getString(R.string.ok)) { _, _ -> }
                show()
            }
        }
    }

    private fun showError(message: Int) {
        runOnUiThread {
            AlertDialog.Builder(this, R.style.BottomDialogStyle).apply {
                setMessage(message)
                setPositiveButton(getString(R.string.ok)) { _, _ -> }
                show()
            }
        }
    }

    private fun showDone(message: Int) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, ConfigActivity::class.java))
        }
    }
}