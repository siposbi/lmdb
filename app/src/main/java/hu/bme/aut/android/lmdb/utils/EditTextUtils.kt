package hu.bme.aut.android.lmdb.utils

import android.widget.EditText
import androidx.annotation.StringRes

fun EditText.isValid(@StringRes messageRes: Int): Boolean {
    if (text.isEmpty()) {
        requestFocus()
        error = resources.getString(messageRes)
        return false
    }
    return true
}