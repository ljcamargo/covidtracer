package tech.camargo.covid

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import tech.camargo.covid.databinding.FragmentCodeBinding

class CodeFragment(val callback: ((String?)->Unit)): BottomSheetDialogFragment() {

    private lateinit var B: FragmentCodeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View? {
        B = DataBindingUtil.inflate(inflater, R.layout.fragment_code, container, false)
        B.privacy.setOnClickListener {
            ReaderActivity.privacy(this.activity as Activity)
        }
        B.terms.setOnClickListener {
            ReaderActivity.terms(this.activity as Activity)
        }
        B.agree.setOnClickListener {
            callback(B.code)
            dismiss()
        }
        return B.root
    }

    companion object {
        fun create(callback: (String?) -> Unit): CodeFragment {
            return CodeFragment(callback).apply {
                isCancelable = true
            }
        }
    }

}