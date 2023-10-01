package tech.baza_trainee.mama_ne_vdoma.data.utils

import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

fun Request.createEmptyResponse(error: String): Response {
    val body = "NO_BODY".toResponseBody(null)
    return Response.Builder()
        .protocol(Protocol.HTTP_1_1)
        .request(this)
        .code(500)
        .body(body)
        .message(String.format("ERROR_MESSAGE: %s", error))
        .build()
}