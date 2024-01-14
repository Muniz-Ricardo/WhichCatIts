package com.ricardomuniz.whichcatits.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ricardomuniz.whichcatits.data.model.Cat
import com.ricardomuniz.whichcatits.domain.model.State
import com.ricardomuniz.whichcatits.domain.usecase.GetCatListUseCase
import com.ricardomuniz.whichcatits.domain.usecase.GetCatMoreListUseCase
import kotlinx.coroutines.launch

class CatListViewModel(
    private val catListUseCase: GetCatListUseCase,
    private val catMoreListUseCase: GetCatMoreListUseCase
) : ViewModel() {

    private val _loadingState = MutableLiveData<State>()
    val loadingState: LiveData<State> get() = _loadingState

    private val _loadingStateEndless = MutableLiveData<State>()
    val loadingStateEndless: LiveData<State> get() = _loadingStateEndless

    private val _catList = MutableLiveData<ArrayList<Cat>>()
    val catList: LiveData<ArrayList<Cat>> get() = _catList

    fun getCatList(limit: Int, page: Int) = viewModelScope.launch {
        _loadingState.value = State.Loading
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
    }

    fun getMoreCatList(limit: Int, page: Int) = viewModelScope.launch {
        _loadingStateEndless.value = State.Loading
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
    }
}