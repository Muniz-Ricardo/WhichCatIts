package com.ricardomuniz.whichcatits.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ricardomuniz.whichcatits.data.model.Cat
import com.ricardomuniz.whichcatits.domain.model.State
import com.ricardomuniz.whichcatits.domain.usecase.GetCatListUseCase
import kotlinx.coroutines.launch

class CatListViewModel(
    private val catListUseCase: GetCatListUseCase
) : ViewModel() {

    private val _loadingState = MutableLiveData<State>()
    val loadingState: LiveData<State> get() = _loadingState

    private val _catList = MutableLiveData<ArrayList<Cat>>()
    val catList: LiveData<ArrayList<Cat>> get() = _catList

    fun getCatList(limit: Int, page: Int) = viewModelScope.launch {
        _loadingState.value = State.Loading
        try {
            val result = catListUseCase.invoke(limit, page)
            _catList.value = result.body()
            _loadingState.value = State.Success(message = "Response successful to get data.")
        } catch (e: Exception) {
            _loadingState.value =
                State.Error(errorMessage = e.message ?: "Unknown Error!")
        }
    }
}