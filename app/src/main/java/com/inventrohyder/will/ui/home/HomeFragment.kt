package com.inventrohyder.will.ui.home

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.inventrohyder.will.R
import com.inventrohyder.will.WillApplication
import com.inventrohyder.will.WillViewModel
import com.inventrohyder.will.WillViewModelFactory
import com.inventrohyder.will.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val willViewModel: WillViewModel by viewModels {
        WillViewModelFactory((activity?.application as WillApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val tvLatestWill = root.findViewById<TextView>(R.id.tvLatestWill)

        // Add an observer on the LiveData returned by getLatestWill
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        willViewModel.latestWill.observe(viewLifecycleOwner) { wills ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tvLatestWill.text = Html.fromHtml(wills[0].will, Html.FROM_HTML_MODE_LEGACY)
            } else {
                tvLatestWill.text = Html.fromHtml(wills[0].will)
            }
        }


        val fab = root.findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            findNavController().navigate(R.id.navigation_new_will)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}