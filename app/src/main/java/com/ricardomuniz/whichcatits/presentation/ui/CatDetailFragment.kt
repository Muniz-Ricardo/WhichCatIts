package com.ricardomuniz.whichcatits.presentation.ui

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.ricardomuniz.whichcatits.databinding.FragmentCatDetailBinding
import com.ricardomuniz.whichcatits.domain.model.State
import com.ricardomuniz.whichcatits.presentation.viewmodel.CatDetailViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CatDetailFragment : Fragment() {

    private val catDetailViewModel: CatDetailViewModel by viewModel {
        parametersOf(requireContext())
        parametersOf(requireActivity())
    }

    private val args: CatDetailFragmentArgs by navArgs()

    private var _binding: FragmentCatDetailBinding? = null
    private val binding get() = _binding!!

    companion object {
        var IMAGE_BITMAP: Bitmap? = null
        const val SIZE_IMAGE = 200
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCatDetailBinding.inflate(layoutInflater).apply {
            lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        observerData()
    }

    private fun initData() {
        catDetailViewModel.getCatById(args.catId)

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnShareImage.setOnClickListener {
            if (IMAGE_BITMAP != null)
                catDetailViewModel.shareCatImage(IMAGE_BITMAP!!)
        }

        binding.errorView.btnReload.setOnClickListener {
            reloadScreen()
        }
    }

    private fun observerData() {
        catDetailViewModel.loadingState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is State.Loading -> {}

                is State.Success -> {
                    showError(isError = false)
                }

                is State.Error -> {
                    showError(isError = true)
                }
            }
        }

        catDetailViewModel.catData.observe(viewLifecycleOwner) { cat ->
            loadDetailImage(cat.url)
        }
    }

    private fun showError(isError: Boolean) {
        binding.errorView.root.visibility =
            if (isError) VISIBLE else GONE
    }

    private fun reloadScreen() {
        binding.errorView.root.visibility = GONE
        catDetailViewModel.getCatById(args.catId)
    }

    private fun loadDetailImage(
        url: String?
    ) = lifecycleScope.launch {
        withContext(Dispatchers.Main) {
            requireActivity().runOnUiThread {
                Glide.with(requireContext())
                    .asBitmap()
                    .load(url)
                    .placeholder(null)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .apply(
                        RequestOptions().format(
                            DecodeFormat.PREFER_ARGB_8888
                        )
                    )
                    .into(object : CustomTarget<Bitmap?>(SIZE_IMAGE, SIZE_IMAGE) {
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap?>?,
                        ) {
                            resource.let {
                                binding.ivCatDetail.setImageBitmap(it)
                                IMAGE_BITMAP = it
                            }

                        }

                        override fun onLoadCleared(placeholder: Drawable?) {}

                    })

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}