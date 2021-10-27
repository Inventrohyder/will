package com.inventrohyder.will.ui.home

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.inventrohyder.will.*
import com.inventrohyder.will.databinding.FragmentNewWillBinding

class NewWillFragment : Fragment() {

    private lateinit var editWordView: EditText
    private var _binding: FragmentNewWillBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val willViewModel: WillViewModel by viewModels {
        WillViewModelFactory((activity?.application as WillApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentNewWillBinding.inflate(inflater, container, false)
        val root: View = binding.root
        editWordView = root.findViewById(R.id.edit_will)

        val button = root.findViewById<Button>(R.id.button_save)
        button.setOnClickListener {
            if (TextUtils.isEmpty(editWordView.text)) {
                Toast.makeText(
                    context,
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG
                ).show()
            } else {
                val will = Will(editWordView.text.toString())
                willViewModel.insert(will)
            }
            findNavController().navigate(R.id.navigation_home)
        }
        return root
    }
}