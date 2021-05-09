package hu.bme.aut.android.kliensalk_hf_2_android.utils

import android.app.Activity
import android.content.Context
import android.view.View
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT
import com.google.android.material.snackbar.Snackbar

fun Activity.showShortSnackbar(msg: String) {
    showSnackBar(currentFocus ?: View(this), msg, LENGTH_SHORT)
}

fun Activity.showLongSnackbar(msg: String) {
    showSnackBar(currentFocus ?: View(this), msg, LENGTH_LONG)
}

private fun Context.showSnackBar(view: View, msg: String, length: Int) {
    Snackbar.make(view, msg, length).show()
}