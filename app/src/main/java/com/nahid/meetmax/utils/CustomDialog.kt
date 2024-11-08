package com.nahid.meetmax.utils

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import androidx.core.content.ContextCompat


class CustomDialog {

    companion object {
        fun createDialog(
            context: Context,
            title: String,
            message: String,
            buttonColor: Int,
            action: (Boolean) -> Unit
        ) {

            val builder = AlertDialog.Builder(context)
                .apply {
                    setTitle(title)
                    setMessage(message)
                    setPositiveButton("Yes") { dialogInterface, i ->
                        action(true)
                        dialogInterface.dismiss()
                    }
                    setNegativeButton("No") { dialogInterface, i ->
                        action(false)
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


}