package tech.camargo.covid

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.zxing.client.android.BeepManager
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import org.koin.android.ext.android.inject
import permissions.dispatcher.*
import tech.camargo.covid.databinding.ActivityMainBinding
import tech.camargo.covid.utils.Linter

@RuntimePermissions
class MainActivity : AppCompatActivity() {

    private lateinit var B: ActivityMainBinding
    private val beepManager by lazy { BeepManager(this) }
    private val persistent: Persistent by inject()
    private val constants: Constants by inject()
    private val linter: Linter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        B = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(B.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        B.toolbar.setLogo(R.drawable.logo)
        initQRCameraWithPermissionCheck()
    }

    override fun onResume() {
        super.onResume()
        initQRCamera()
    }

    override fun onPause() {
        super.onPause()
        B.barcodeScanner.pause()
    }

    override fun onKeyDown(code: Int, event: KeyEvent?): Boolean {
        return B.barcodeScanner.onKeyDown(code, event) || super.onKeyDown(code, event)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        when (constants.alternateMethod()) {
            Constants.AlternateMethod.SMS_AUTO -> menu?.findItem(R.id.manual)?.isVisible = true
            Constants.AlternateMethod.SMS_INTENT -> menu?.findItem(R.id.sms)?.isVisible = true
            Constants.AlternateMethod.NONE -> {
                menu?.findItem(R.id.manual)?.isVisible = false
                menu?.findItem(R.id.sms)?.isVisible = false
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.manual -> {
                manualCodeWithPermissionCheck()
                true
            }
            R.id.sms -> {
                smsIntent()
                true
            }
            R.id.settings -> {
                ConfigActivity.start(this)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    fun initQRCamera() {
        B.barcodeScanner.apply {
            decoderFactory = DefaultDecoderFactory(constants.supportedCodes())
            initializeFromIntent(intent)
            decodeSingle { result ->
                Log.v(TAG, "Scanned QR => ${result.text}")
                if (linter.lintQR(result.text)) {
                    beepManager.playBeepSoundAndVibrate()
                    onQR(result.text)
                } else {
                    showError(R.string.qr_wrong)
                }
            }
            resume()
        }
    }

    @OnShowRationale(Manifest.permission.CAMERA)
    fun cameraRationale(request: PermissionRequest) {
        permissionRationale(request, R.string.camera_permission_rationale)
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    fun cameraDeniedOnce() { manualCodeWithPermissionCheck() }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    fun cameraDeniedEver() { manualCodeWithPermissionCheck() }

    private fun onQR(code: String) {
        if (persistent.firstTime) {
            phone { sendQR(code) }
        } else {
            sendQR(code)
        }
    }

    private fun sendQR(code: String) {
        ResultActivity.startWithQR(this@MainActivity, code)
        finish()
    }

    private fun phone(done: () -> Unit) {
        val fragment = PhoneFragment.create({
            initQRCamera()
        }) { phone ->
            if (linter.lintPhone(phone)) {
                persistent.cellphones = phone
                done()
            } else {
                showError(R.string.phone_wrong)
                phone(done)
            }
        }
        fragment.show(supportFragmentManager, "1984")
    }

    private fun smsIntent() {
        val address = constants.smsTarget()
        val fragment = CodeFragment.create { code ->
            if (linter.lintCode(code)) {
                Intent(Intent.ACTION_VIEW).apply {
                    type = "vnd.android-dir/mms-sms"
                    putExtra("address", address)
                    putExtra("sms_body", code)
                    startActivity(this)
                }
                finish()
            } else {
                showError(R.string.code_wrong)
            }
        }
        fragment.show(supportFragmentManager, FRAGMENT)
    }

    @NeedsPermission(Manifest.permission.SEND_SMS)
    fun manualCode() {
        val fragment = CodeFragment.create { code ->
            if (linter.lintCode(code)) {
                ResultActivity.startWithCode(this@MainActivity, code!!)
                finish()
            } else {
                showError(R.string.code_wrong)
            }
        }
        fragment.show(supportFragmentManager, FRAGMENT)
    }

    @OnShowRationale(Manifest.permission.SEND_SMS)
    fun smsRationale(request: PermissionRequest) =
        permissionRationale(request, R.string.sms_permissions_rationale)

    @OnPermissionDenied(Manifest.permission.SEND_SMS)
    fun smsDeniedOnce() {
        Toast.makeText(this, R.string.sms_no_permission, Toast.LENGTH_LONG).show()
    }

    @OnNeverAskAgain(Manifest.permission.SEND_SMS)
    fun smsEver() {
        Toast.makeText(this, R.string.sms_no_permission, Toast.LENGTH_LONG).show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    private fun permissionRationale(request: PermissionRequest, text: Int) {
        runOnUiThread {
            AlertDialog.Builder(this, R.style.AlertDialogStyle).apply {
                setMessage(text)
                setPositiveButton(R.string.ok) { _, _ -> request.proceed() }
                setNegativeButton(R.string.cancel) { _, _ -> request.cancel() }
                show()
            }
        }
    }

    private fun showError(message: Int) {
        runOnUiThread {
            AlertDialog.Builder(this, R.style.AlertDialogStyle).apply {
                setMessage(message)
                setPositiveButton(getString(R.string.ok)) { _, _ -> }
                show()
            }
        }
    }

    companion object {
        const val TAG = "MainActivity"
        const val FRAGMENT ="1984"
        fun start(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }

}