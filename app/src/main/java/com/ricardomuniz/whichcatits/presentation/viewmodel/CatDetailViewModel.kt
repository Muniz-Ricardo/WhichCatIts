package com.ricardomuniz.whichcatits.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.ricardomuniz.whichcatits.domain.usecase.GetCatDetailUseCase

class CatDetailViewModel(
    private val catDetailUseCase: GetCatDetailUseCase
) :
    ViewModel() {

}