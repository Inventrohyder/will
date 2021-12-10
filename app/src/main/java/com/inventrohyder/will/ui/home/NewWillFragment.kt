package com.inventrohyder.will.ui.home

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.inventrohyder.will.*
import com.inventrohyder.will.databinding.FragmentNewWillBinding
import jp.wasabeef.richeditor.RichEditor


class NewWillFragment : Fragment() {

    private lateinit var editWordView: RichEditor
    private lateinit var homeViewModel: HomeViewModel
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
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        // Inflate the layout for this fragment
        _binding = FragmentNewWillBinding.inflate(inflater, container, false)
        val root: View = binding.root
        editWordView = root.findViewById(R.id.edit_will)

        // Match text editor background color to that chosen by the OS
        val colorSurface = TypedValue()
        requireContext().theme.resolveAttribute(R.attr.colorSurface, colorSurface, true)
        editWordView.setEditorBackgroundColor(
            colorSurface.data
        )
        editWordView.setBackground("")

        // Match text editor font color to that chosen by the OS
        val colorOnSurface = TypedValue()
        requireContext().theme.resolveAttribute(R.attr.colorOnSurface, colorOnSurface, true)
        editWordView.setEditorFontColor(
            colorOnSurface.data
        )

        // Update the view model when the texts changes
        editWordView.setOnTextChangeListener { text ->
            if (text.isEmpty()) {
                editWordView.setPlaceholder(getString(R.string.hint_will))
            }
            homeViewModel.setText(text)
        }

        // Set the default will text to be the latest will
        // If there is no previous will then just show some place holder text.
        willViewModel.latestWill.observe(viewLifecycleOwner, object : Observer<List<Will>> {
            override fun onChanged(t: List<Will>?) {
                if (t != null) {
                    homeViewModel.setText(t[0].will)

                    editWordView.html = t[0].will
                }
                willViewModel.latestWill.removeObserver(this)
            }
        })

        val button = root.findViewById<Button>(R.id.button_save)
        button.setOnClickListener {
            if (homeViewModel.text.value?.isEmpty() == true) {
                Toast.makeText(
                    context,
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG
                ).show()
            } else {
                val will = homeViewModel.text.value?.let { it1 -> Will(it1) }
                if (will != null) {
                    willViewModel.insert(will)
                }
            }
            findNavController().navigate(R.id.navigation_home)
        }

        binding.toggleButton.addOnButtonCheckedListener { toggleButton, checkedId, isChecked ->
            // Respond to button selection
            when (checkedId) {
                R.id.action_bold -> editWordView.setBold()
                R.id.action_italic -> editWordView.setItalic()
                R.id.action_underline -> editWordView.setUnderline()
                R.id.action_undo -> editWordView.undo()
                R.id.action_redo -> editWordView.redo()
            }
        }
        return root
    }

    override fun onResume() {
        super.onResume()
        if (homeViewModel.text.value!!.isNotEmpty()) {
            editWordView.html = homeViewModel.text.value
        }
    }
}