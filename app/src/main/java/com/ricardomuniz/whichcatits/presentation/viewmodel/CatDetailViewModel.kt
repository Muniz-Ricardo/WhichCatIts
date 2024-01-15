package com.ricardomuniz.whichcatits.presentation.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ricardomuniz.whichcatits.data.model.Cat
import com.ricardomuniz.whichcatits.domain.model.State
import com.ricardomuniz.whichcatits.domain.usecase.GetCatDetailUseCase
import com.ricardomuniz.whichcatits.domain.usecase.ShareImageUseCase
import com.ricardomuniz.whichcatits.util.GetStateConnection
import kotlinx.coroutines.launch

class CatDetailViewModel(
    private val catDetailUseCase: GetCatDetailUseCase,
    private val shareImageUseCase: ShareImageUseCase,
    private val getStateConnection: GetStateConnection
) :
    ViewModel() {

    private val _loadingState = MutableLiveData<State>()
    val loadingState: LiveData<State> get() = _loadingState

    private val _catData = MutableLiveData<Cat>()
    val catData: LiveData<Cat> get() = _catData

    fun getCatById(catId: String) = viewModelScope.launch {
        _loadingState.value = State.Loading
        if (isNetworkAvailable()) {
            try {
                val result = catDetailUseCase.invoke(id = catId)
                _catData.value = result.body()

                _loadingState.value =
                    State.Success(message = "Data response success by id")
            } catch (e: Exception) {
                _loadingState.value =
                    State.Error(errorMessage = e.message ?: "Unknown Error")
            }
        } else {
            _loadingState.value =
                State.Error(errorMessage = "Connection not available!")
        }
    }

    private fun isNetworkAvailable() =
        getStateConnection.isInternetAvailable()

    fun shareCatImage(bitmap: Bitmap) = viewModelScope.launch {
        shareImageUseCase.execute(bitmap)
    }
}