package tech.baza_trainee.mama_ne_vdoma.presentation.utils

import kotlinx.coroutines.CoroutineScope

class NetworkRequestBuilder<T> {
    internal var builderLoading : ((Boolean) -> Unit)? = null
    internal var builderApiCall: (suspend CoroutineScope.() -> RequestResult<T>)? = null
    internal var builderStart: (() -> Unit)? = null
    internal var builderFinish: (() -> Unit)? = null
    internal var builderSuccess: ((result: T) -> Unit) = {}
    internal var builderError: ((String) -> Unit)? = null
    internal var builderErrorWithCode: ((String, Int) -> Unit)? = null
}

fun <T> NetworkRequestBuilder<T>.execute(execution: (suspend CoroutineScope.() -> RequestResult<T>)?) {
    builderApiCall = execution
}

fun <T> NetworkRequestBuilder<T>.onError(onError: (String) -> Unit) {
    builderError = onError
}

fun <T> NetworkRequestBuilder<T>.onErrorWithCode(onErrorWithCode: (String, Int) -> Unit) {
    builderErrorWithCode = onErrorWithCode
}

fun <T> NetworkRequestBuilder<T>.onFinish(onFinish: () -> Unit) {
    builderFinish = onFinish
}

fun <T> NetworkRequestBuilder<T>.onStart(onStart: () -> Unit) {
    builderStart = onStart
}

fun <T> NetworkRequestBuilder<T>.onSuccess(onSuccess: (T) -> Unit) {
    builderSuccess = onSuccess
}

fun <T> NetworkRequestBuilder<T>.onLoading(onLoading: (Boolean) -> Unit){
    builderLoading = onLoading
}
