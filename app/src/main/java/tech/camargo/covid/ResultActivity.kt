package tech.camargo.covid


import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import org.koin.android.ext.android.inject
import tech.camargo.covid.databinding.ActivityResultBinding
import tech.camargo.covid.viewmodels.ResultViewModel

class ResultActivity : AppCompatActivity() {

    private lateinit var B: ActivityResultBinding
    private val M: ResultViewModel by inject()
    private val code by lazy { intent?.getStringExtra(CODE) }
    private val qr by lazy { intent?.getStringExtra(QR) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        B = DataBindingUtil.setContentView(this, R.layout.activity_result)
        setSupportActionBar(B.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        title = ""
        B.lifecycleOwner = this
        B.viewModel = M
        M.wait.observe(this) {
            if (it) visualState(loading = true, success = false)
        }
        M.status.observe(this) {
            visualState(loading = false, success = it.success)
            if (!it.success) showErrorRetry()
        }
        submit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.settings -> {
                ConfigActivity.start(this)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.result, menu)
        return true
    }

    private fun submit() {
        when {
            code?.isNotEmpty() == true -> {
                Log.v(TAG, "Received QR data => $qr")
                M.submitCode(code!!)
            }
            qr?.isNotEmpty() == true -> {
                Log.v(TAG, "Received CODE data => $code")
                M.submitQR(qr!!)
            }
            else -> {
                MainActivity.start(this)
                finish()
            }
        }
    }

    private fun visualState(loading: Boolean, success: Boolean) {
        val color = ContextCompat.getColor(this, when {
            loading -> R.color.clear
            !loading && success -> R.color.scrim_success
            !loading && !success -> R.color.scrim_error
            else -> R.color.clear
        })
        B.collapsingToolbar.apply {
            contentScrim = ColorDrawable(color)
            setStatusBarScrimColor(color)
        }
    }

    fun restart(view: View) {
        MainActivity.start(this)
    }

    fun recent(view: View) {
        ListActivity.start(this)
    }

    fun end(view: View) {
        finish()
    }

    private fun showErrorRetry() {
        runOnUiThread {
            AlertDialog.Builder(this, R.style.BottomDialogStyle).apply {
                setMessage(R.string.conn_err)
                setPositiveButton(getString(R.string.retry)) { _, _ -> submit() }
                setPositiveButton(getString(R.string.cancel)) { _, _ -> }
                show()
            }
        }
    }

    companion object {
        const val TAG = "ResultActivity"
        const val CODE = "CODE"
        const val QR = "QR"
        fun startWithQR(context: Context, text: String) {
            context.startActivity(Intent(context, ResultActivity::class.java).apply {
                putExtra(QR, text)
            })
        }
        fun startWithCode(context: Context, text: String) {
            context.startActivity(Intent(context, ResultActivity::class.java).apply {
                putExtra(CODE, text)
            })
        }
    }
}