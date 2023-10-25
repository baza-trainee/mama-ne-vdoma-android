package tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions

fun <T: Any> List<T>.indexOrZero(element: T) = indexOf(element).takeIf { it != -1 } ?: 0