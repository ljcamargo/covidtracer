package tech.camargo.covid.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class Adapter<T>(
    private val layout: Int,
    private val variable: Int,
    private val items: ArrayList<T> = arrayListOf(),
    private val listener: ((View, Int) -> Unit)? = null
): RecyclerView.Adapter<Adapter<T>.ViewHolder>() {

    var onHolder: ((ViewHolder, Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewDataBinding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context), layout, parent, false
        )
        return ViewHolder(viewDataBinding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val viewDataBinding = holder.viewDataBinding
        items.let {viewDataBinding.setVariable(variable, it[position]) }
        listener?.let { listener ->
            holder.viewDataBinding.root.setOnClickListener { listener(it, position) }
        }
        onHolder?.invoke(holder, position)
    }

    fun update(items: List<T>) {
        this.items.clear()
        this.items.addAll(items)
        this.notifyDataSetChanged()
    }

    inner class ViewHolder(val viewDataBinding: ViewDataBinding):
        RecyclerView.ViewHolder(viewDataBinding.root) {
        init {
            this.viewDataBinding.executePendingBindings()
        }
    }
}