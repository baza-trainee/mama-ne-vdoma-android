package tech.baza_trainee.mama_ne_vdoma.presentation.utils

fun String?.orDefault(default: String) = orEmpty().ifEmpty { default }