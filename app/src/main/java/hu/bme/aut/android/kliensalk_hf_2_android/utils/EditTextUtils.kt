package hu.bme.aut.android.kliensalk_hf_2_android.utils

import android.widget.EditText

fun EditText.isValid(errorMsgResource: Int): Boolean {
    if (text.isEmpty()) {
        requestFocus()
        error = this.resources.getString(errorMsgResource)
        return false
    }
    return true
}