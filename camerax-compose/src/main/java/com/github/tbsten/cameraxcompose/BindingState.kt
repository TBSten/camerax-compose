package com.github.tbsten.cameraxcompose

sealed interface BindingState {
    data object Initial : BindingState

    interface Finish : BindingState

    data object Failed : Finish

    data object Success : Finish
}
