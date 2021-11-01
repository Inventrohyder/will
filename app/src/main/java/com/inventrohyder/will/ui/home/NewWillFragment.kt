package com.inventrohyder.will.ui.home

import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.CharacterStyle
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
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

    private var willText: String = ""
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
//        editWordView.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//                willText = s.toString()
//            }
//        })

        val button = root.findViewById<Button>(R.id.button_save)
        button.setOnClickListener {
            if (TextUtils.isEmpty(editWordView.text)) {
                Toast.makeText(
                    context,
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG
                ).show()
            } else {
                val will = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Will(Html.toHtml(editWordView.text, Html.TO_HTML_PARAGRAPH_LINES_INDIVIDUAL))
                } else {
                    Will(Html.toHtml(editWordView.text))
                }
                willViewModel.insert(will)
            }
            findNavController().navigate(R.id.navigation_home)
        }

        binding.toggleButton.addOnButtonCheckedListener { toggleButton, checkedId, isChecked ->
            // Respond to button selection
            when (checkedId) {
                R.id.action_bold -> setBold()
                R.id.action_italic -> setItalic()
                R.id.action_underline -> setUnderline()
//                R.id.action_bold -> editWordView.setBold()
//                R.id.action_italic -> editWordView.setItalic()
//                R.id.action_underline -> editWordView.setUnderline()
//                R.id.action_undo -> editWordView.undo()
//                R.id.action_redo -> editWordView.redo()
//                R.id.action_subscript -> editWordView.setSubscript()
//                R.id.action_superscript -> editWordView.setSuperscript()
            }
        }
        return root
    }


    private fun setStyle(style: CharacterStyle) {
        val spannableString: Spannable = SpannableStringBuilder(editWordView.text)
        spannableString.setSpan(
            style,
            editWordView.selectionStart,
            editWordView.selectionEnd,
            0
        )
        val cursorLocation = editWordView.selectionEnd
        editWordView.setText(spannableString)

        // Move the cursor to the old location
        editWordView.setSelection(cursorLocation)
    }

    private fun setBold() {
        setStyle(StyleSpan(Typeface.BOLD))
    }

    private fun setItalic() {
        setStyle(StyleSpan(Typeface.ITALIC))
    }

    private fun setUnderline() {
        setStyle(UnderlineSpan())
    }
}