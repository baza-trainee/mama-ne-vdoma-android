package tech.baza_trainee.mama_ne_vdoma.data.utils

import com.google.gson.Gson
import okhttp3.ResponseBody

fun ResponseBody.asCustomResponse(): CustomResponse =
    Gson().fromJson(this.string(), CustomResponse::class.java)

fun CustomResponse?.getMessage() = this?.message?.first().orEmpty()