package tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.NetworkRequestBuilder
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestResult

fun <T> ViewModel.networkExecutor(networkBuilder: NetworkRequestBuilder<T>.() -> Unit) {
    viewModelScope.networkOperation(networkBuilder)
}

fun <T> CoroutineScope.networkOperation(networkRequestBuilder: NetworkRequestBuilder<T>.() -> Unit) {

    val builder = NetworkRequestBuilder<T>().apply { networkRequestBuilder() }

    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        var message = ""
        var code = 0
        when (throwable) {
            is Exception -> {
                message = throwable.message.orEmpty()
                code = if (throwable is HttpException) throwable.code() else 0
            }

            is Error -> {
                message = throwable.message.orDefault("defaultErrorMessage")
            }
        }

        with(builder) {
            builderError?.invoke(message)
            builderErrorWithCode?.invoke(message, code)
            builderFinish?.invoke()
            builderLoading?.invoke(false)
        }
    }

    launch(exceptionHandler + Dispatchers.IO) {
        builder.builderStart?.invoke()

        builder.builderLoading?.invoke(true)

        val requestResult = builder.builderApiCall?.invoke(this@launch)

        if (requestResult == null)
            builder.builderError?.invoke("defaultErrorMessage")
        else {
            when (requestResult) {
                is RequestResult.Success -> {
                    builder.builderSuccess.invoke(requestResult.result)
                }

                is RequestResult.Error -> {
                    builder.builderError?.invoke(
                        requestResult.error.ifEmpty { "defaultErrorMessage" }
                    )
                    builder.builderErrorWithCode?.invoke(
                        requestResult.error.ifEmpty { "defaultErrorMessage" },
                        requestResult.code
                    )
                }
            }
        }

        builder.builderFinish?.invoke()

        builder.builderLoading?.invoke(false)
    }
}