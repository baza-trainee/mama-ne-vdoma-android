package tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.widget.Toast

fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("Permissions should be called in the context of an Activity")
}

fun Context.showToast(message: String) {
    if (message.isNotBlank()) Toast.makeText(
        this,
        message,
        Toast.LENGTH_LONG
    ).show()
}