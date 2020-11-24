package tech.camargo.covid

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import org.koin.android.ext.android.inject
import tech.camargo.covid.databinding.ActivityReaderBinding

class ReaderActivity : AppCompatActivity() {

    private lateinit var B: ActivityReaderBinding
    private val content by lazy { intent?.getStringExtra(CONTENT) }
    private val url by lazy { intent?.getStringExtra(URL) }
    private val constants: Constants by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        B = DataBindingUtil.setContentView(this, R.layout.activity_reader)
        setSupportActionBar(B.toolbar)
        title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        B.webView.webViewClient = object: WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                B.progress.visibility = View.VISIBLE
            }
            override fun onPageFinished(view: WebView?, url: String?) {
                B.progress.visibility = View.GONE
                title = view?.title ?: title
            }
        }
        load()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun load() {
        val uri = when {
            url != null -> url
            content != null -> constants.getURI(content)
            else -> null
        }
        Log.v(TAG,"loading $content $uri")
        B.webView.loadUrl(uri)
    }

    companion object {
        const val TAG = "ReaderActivity"
        const val CONTENT = "content"
        const val ABOUT = "about"
        const val TERMS = "terms"
        const val PRIVACY = "privacy"
        const val URL = "url"

        fun withUrl(context: Context, url: String) {
            context.startActivity(Intent(context, ReaderActivity::class.java).apply {
                putExtra(URL, url)
            })
        }

        fun about(context: Context) {
            context.startActivity(Intent(context, ReaderActivity::class.java).apply {
                putExtra(CONTENT, ABOUT)
            })
        }

        fun terms(context: Context) {
            context.startActivity(Intent(context, ReaderActivity::class.java).apply {
                putExtra(CONTENT, TERMS)
            })
        }

        fun privacy(context: Context) {
            context.startActivity(Intent(context, ReaderActivity::class.java).apply {
                putExtra(CONTENT, PRIVACY)
            })
        }
    }
}