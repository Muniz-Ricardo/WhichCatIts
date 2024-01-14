package com.ricardomuniz.whichcatits.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ricardomuniz.whichcatits.data.model.Cat
import com.ricardomuniz.whichcatits.databinding.FragmentCatListBinding
import com.ricardomuniz.whichcatits.domain.model.State.Error
import com.ricardomuniz.whichcatits.domain.model.State.Loading
import com.ricardomuniz.whichcatits.domain.model.State.Success
import com.ricardomuniz.whichcatits.presentation.adapter.CatListAdapter
import com.ricardomuniz.whichcatits.presentation.viewmodel.CatListViewModel
import com.ricardomuniz.whichcatits.util.OnItemClickListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class CatListFragment : Fragment(), OnItemClickListener {

    private val catListViewModel: CatListViewModel by viewModel()

    private lateinit var adapter: CatListAdapter

    private var _binding: FragmentCatListBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCatListBinding.inflate(layoutInflater).apply {
            lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observerData()
    }

    private fun initData() {
        catListViewModel.getCatList(0, 0)
    }

    private fun observerData() {
        catListViewModel.loadingState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Loading -> {
                    showLoading(isLoading = true)
                }

                is Success -> {
                    showLoading(isLoading = false)
                    binding.recyclerCatList.visibility = VISIBLE
                }

                is Error -> {
                    showLoading(isLoading = true)
                }
            }
        }

        catListViewModel.catList.observe(viewLifecycleOwner) { catList ->
            setAdapter(catList)
        }
    }

    private fun setAdapter(catList: ArrayList<Cat>) {
        val manager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL, false
        )
        adapter = CatListAdapter(requireContext(), catList.toMutableList(), this)
        binding.recyclerCatList.layoutManager = manager
        binding.recyclerCatList.adapter = adapter
        adapter.notifyItemRangeChanged(0, catList.size)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility =
            if (isLoading) VISIBLE else GONE
    }

    override fun onClick(catId: String) {
        val action = CatListFragmentDirections.actionCatListFragmentToCatDetailFragment(catId)
        findNavController().navigate(action)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}