package com.ricardomuniz.whichcatits.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ricardomuniz.whichcatits.data.model.Cat
import com.ricardomuniz.whichcatits.domain.model.State
import com.ricardomuniz.whichcatits.domain.usecase.GetCatListUseCase
import com.ricardomuniz.whichcatits.domain.usecase.GetCatMoreListUseCase
import com.ricardomuniz.whichcatits.util.GetStateConnection
import kotlinx.coroutines.launch

class CatListViewModel(
    private val catListUseCase: GetCatListUseCase,
    val catMoreListUseCase: GetCatMoreListUseCase,
    private val getStateConnection: GetStateConnection
) : ViewModel() {

    private val _loadingState = MutableLiveData<State>()
    val loadingState: LiveData<State> get() = _loadingState

    private val _loadingStateEndless = MutableLiveData<State>()
    val loadingStateEndless: LiveData<State> get() = _loadingStateEndless

    private val _catList = MutableLiveData<ArrayList<Cat>>()
    val catList: LiveData<ArrayList<Cat>> get() = _catList

    fun getCatList(limit: Int, page: Int) = viewModelScope.launch {
        _loadingState.value = State.Loading
        if (isNetworkAvailable()) {
            try {
                val result = catListUseCase.invoke(limit, page)
                val updatedList = result.body() ?: arrayListOf()

                _catList.value?.let { existingList ->
                    existingList.addAll(updatedList)
                    _catList.value = existingList
                } ?: run {
                    _catList.value = updatedList
                }

                _loadingState.value =
                    State.Success(message = "Response successful to get data.")
            } catch (e: Exception) {
                _loadingState.value =
                    State.Error(errorMessage = e.message ?: "Unknown Error!")
            }
        } else {
            _loadingState.value =
                State.Error(errorMessage = "Error to load! Connectivity problem.")
        }
    }

    fun getMoreCatList(limit: Int, page: Int) = viewModelScope.launch {
        _loadingStateEndless.value = State.Loading
        if (isNetworkAvailable()) {
            try {
                val result = catMoreListUseCase.invoke(limit, page)
                val updatedList = result.body() ?: arrayListOf()

                _catList.value?.let { existingList ->
                    existingList.addAll(updatedList)
                    _catList.value = existingList
                } ?: run {
                    _catList.value = updatedList
                }

                _loadingStateEndless.value =
                    State.Success(message = "Response successful to get more data.")
            } catch (e: Exception) {
                _loadingStateEndless.value =
                    State.Error(errorMessage = e.message ?: "Unknown Error!")
            }
        } else {
            _loadingStateEndless.value =
                State.Error(errorMessage = "Error to load more items! Connectivity problem.")
        }
    }

    fun isNetworkAvailable() =
        getStateConnection.isInternetAvailable()
}