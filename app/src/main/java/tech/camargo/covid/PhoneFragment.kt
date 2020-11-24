package tech.camargo.covid

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.android.ext.android.inject
import tech.camargo.covid.databinding.FragmentPhoneBinding
import tech.camargo.covid.utils.Linter

class PhoneFragment(val callback: ((String?)->Unit)): BottomSheetDialogFragment() {

    private lateinit var B: FragmentPhoneBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View? {
        B = DataBindingUtil.inflate(inflater, R.layout.fragment_phone, container, false)
        B.privacy.setOnClickListener {
            ReaderActivity.privacy(this.activity as Activity)
        }
        B.terms.setOnClickListener {
            ReaderActivity.terms(this.activity as Activity)
        }
        B.agree.setOnClickListener {
            callback(B.cellphone)
            dismiss()
        }
        return B.root
    }

    companion object {
        const val TAG = "PhoneFragment"
        fun create(callback: (String?) -> Unit): PhoneFragment {
            return PhoneFragment(callback)
        }
    }

}