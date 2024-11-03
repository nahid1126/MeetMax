package com.nahid.meetmax.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

object Tools {
    fun setSystemBarColor(act: Activity, @ColorRes color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = act.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = act.resources.getColor(color)
        }
    }

    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity.currentFocus
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun createPositiveOnlyDialog(
        context: Context,
        title: String,
        message: String,
        buttonColor: Int,
        action: () -> Unit
    ) {

        val builder = AlertDialog.Builder(context)
            .apply {
                setTitle(title)
                setMessage(message)
                setPositiveButton("Ok") { dialogInterface, i ->
                    action()
                    dialogInterface.dismiss()
                }

                setCancelable(false)
            }


        builder
            .create()
            .apply {
                show()
                getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(
                    ContextCompat.getColor(
                        context,
                        buttonColor
                    )
                )
                getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(
                    ContextCompat.getColor(
                        context,
                        buttonColor
                    )
                )
            }

    }
}