package hu.bme.aut.android.lmdb.utils

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar

fun Activity.showSnackbar(@StringRes messageRes: Int, length: Int = Snackbar.LENGTH_SHORT) {
    showSnackBar(findViewById(android.R.id.content)!!, messageRes, length)
}

fun Activity.showSnackbar(@StringRes messageRes: Int, view: View, length: Int = Snackbar.LENGTH_SHORT) {
    showSnackBar(view, messageRes, length)
}

private fun Context.showSnackBar(view: View, @StringRes messageRes: Int, length: Int) {
    Snackbar.make(view, resources.getString(messageRes), length).show()
}