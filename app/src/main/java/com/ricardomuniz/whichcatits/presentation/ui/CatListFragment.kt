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
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.ricardomuniz.whichcatits.databinding.FragmentCatListBinding
import com.ricardomuniz.whichcatits.domain.model.State.Error
import com.ricardomuniz.whichcatits.domain.model.State.Loading
import com.ricardomuniz.whichcatits.domain.model.State.Success
import com.ricardomuniz.whichcatits.presentation.adapter.CatListAdapter
import com.ricardomuniz.whichcatits.presentation.viewmodel.CatListViewModel
import com.ricardomuniz.whichcatits.util.OnItemClickListener
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class CatListFragment : Fragment(), OnItemClickListener {

    companion object {
        const val PAGE_SIZE_LIMIT = 10
    }

    private val catListViewModel: CatListViewModel by sharedViewModel()

    private lateinit var adapter: CatListAdapter

    private var isLoading = false
    private var isInitialLoad = true
    private var currentPage = 1
    private var lastVisibleItemPosition = 0

    private var _binding: FragmentCatListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCatListBinding.inflate(layoutInflater).apply {
            lifecycleOwner = viewLifecycleOwner
        }

        setupRecyclerView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observerData()

        if (isInitialLoad) {
            initData()
            isInitialLoad = false
        }
    }

    private fun initData() {
        catListViewModel.getCatList(PAGE_SIZE_LIMIT, 0)
    }

    private fun observerData() {
        catListViewModel.loadingState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Loading -> {
                    showLoading(isLoading = true)
                }

                is Success -> {
                    showLoading(isLoading = false)
                }

                is Error -> {
                    showLoading(isLoading = false)
                    showError()
                }
            }
        }

        catListViewModel.loadingStateEndless.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Loading -> {
                    showIndicator(isLoading = true)
                }

                is Success -> {
                    isLoading = false
                    showIndicator(isLoading = false)

                    if (lastVisibleItemPosition != RecyclerView.NO_POSITION) {
                        binding.recyclerCatList.scrollToPosition(lastVisibleItemPosition)
                        lastVisibleItemPosition = RecyclerView.NO_POSITION
                    }
                }

                is Error -> {
                    showIndicator(isLoading = false)
                    isLoading = false

                    Snackbar.make(
                        requireView(), state.errorMessage, Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }

        catListViewModel.catList.observe(viewLifecycleOwner) { catList ->
            adapter.update(catList)
        }
    }

    private fun setupRecyclerView() {
        val manager = LinearLayoutManager(requireContext())
        val initialCatList = catListViewModel.catList.value.orEmpty().toMutableList()
        adapter = CatListAdapter(requireContext(), initialCatList, this)
        binding.recyclerCatList.layoutManager = manager
        binding.recyclerCatList.adapter = adapter

        binding.recyclerCatList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                val isAtEndOfList = lastVisibleItemPosition >= totalItemCount - 1

                if (dy > 0 && isAtEndOfList && !isLoading) {
                    isLoading = true
                    val nextPage = currentPage + 1
                    currentPage = nextPage

                    catListViewModel.getMoreCatList(PAGE_SIZE_LIMIT, nextPage)
                }
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility =
            if (isLoading) VISIBLE else GONE
    }

    private fun showIndicator(isLoading: Boolean) {
        binding.progressBarEndless.visibility =
            if (isLoading) VISIBLE else GONE
    }

    private fun showError() {
        binding.errorView.root.visibility = VISIBLE
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