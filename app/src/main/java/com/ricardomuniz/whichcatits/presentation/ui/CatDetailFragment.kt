package com.ricardomuniz.whichcatits.presentation.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.ricardomuniz.whichcatits.databinding.FragmentCatDetailBinding

class CatDetailFragment : Fragment() {

    private var _binding: FragmentCatDetailBinding? = null
    private val args: CatDetailFragmentArgs by navArgs()

    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCatDetailBinding.inflate(layoutInflater).apply {
            lifecycleOwner = viewLifecycleOwner
        }

        Log.e("catDetail", "args catId: ${args.catId}")

        return binding!!.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}