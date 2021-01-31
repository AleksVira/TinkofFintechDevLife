package com.aleksbsc.tinkoffintechdevlife.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.aleksbsc.tinkoffintechdevlife.R
import com.aleksbsc.tinkoffintechdevlife.databinding.FragmentNetworkErrorBinding
import com.aleksbsc.tinkoffintechdevlife.extensions.setOnSingleClick
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ErrorBottomDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentNetworkErrorBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.bottom_sheet_dialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNetworkErrorBinding.inflate(inflater, container, false)
        binding.root.clipToOutline = true
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.errorRepeat.setOnSingleClick {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}