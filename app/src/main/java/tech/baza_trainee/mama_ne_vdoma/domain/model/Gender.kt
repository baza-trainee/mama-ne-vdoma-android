package tech.baza_trainee.mama_ne_vdoma.domain.model

import androidx.annotation.StringRes
import tech.baza_trainee.mama_ne_vdoma.R

enum class Gender(@StringRes val gender: Int) {
    BOY(R.string.boy), GIRL(R.string.girl), NONE(-1)
}