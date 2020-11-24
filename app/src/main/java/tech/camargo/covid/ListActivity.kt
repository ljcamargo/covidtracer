package tech.camargo.covid

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import org.koin.android.ext.android.inject
import tech.camargo.covid.databinding.ActivityListBinding
import tech.camargo.covid.models.Attendance
import tech.camargo.covid.utils.Adapter


class ListActivity : AppCompatActivity() {

    private lateinit var B: ActivityListBinding
    private val persistent: Persistent by inject()
    private lateinit var adapter: Adapter<Attendance>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        B = DataBindingUtil.setContentView(this, R.layout.activity_list)
        setSupportActionBar(B.toolbar)
        title = getString(R.string.recent_visits)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        adapter = Adapter(R.layout.item_attendance, BR.attendance, arrayListOf())
        B.recycler.adapter = adapter
        B.recycler.addItemDecoration(DividerItemDecoration(this, LinearLayout.VERTICAL))
        load()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.erase_visits -> {
                persistent.resetList()
                showDone(R.string.clear_visits_done)
                load()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.list, menu)
        return true
    }

    private fun load() {
        adapter.update(ArrayList(persistent.attendances.reversed()))
    }

    private fun showDone(message: Int) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    fun scan(view: View) {
        MainActivity.start(this)
        finish()
    }

    companion object {
        const val TAG = "ListActivity"
        fun start(context: Context) {
            context.startActivity(Intent(context, ListActivity::class.java))
        }
    }
}