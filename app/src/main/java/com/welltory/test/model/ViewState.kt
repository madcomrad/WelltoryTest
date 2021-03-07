package com.welltory.test.model

data class ViewState(
    val hintText: String,
    val buttonText: String,
    val isButtonEnabled: Boolean,
    val isButtonVisible: Boolean,
    val resultState: ResultState,
) {
    enum class ResultState {
        NoResult,
        Success,
        Failure;
    }
}
