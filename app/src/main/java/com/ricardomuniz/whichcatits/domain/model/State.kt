package com.ricardomuniz.whichcatits.domain.model

sealed class State {
    data object Loading: State()
    data class Success(val message: String = "") : State()
    data class Error(val errorMessage: String = ""): State()
}
