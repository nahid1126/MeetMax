package com.nahid.meetmax.utils

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.nahid.meetmax.R
import com.nahid.meetmax.databinding.ToastLayoutBinding

enum class Status {
    SUCCESS,FAILED,WARNING
}

class CustomToast {

    companion object{
        fun showToast(context:Context, message:String, status: Status){

            val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            val binding = ToastLayoutBinding.inflate(inflater)

            val layout = binding.toastLayout

            binding.message = message

            when (status){

                Status.SUCCESS ->{
                    layout.background = ContextCompat.getDrawable(context, R.drawable.success_toast_background)
                }

                Status.FAILED -> {
                    layout.background = ContextCompat.getDrawable(context, R.drawable.error_toast_background)
                }

                Status.WARNING -> {
                    layout.background = ContextCompat.getDrawable(context,R.drawable.warning_toast_background)
                }

            }

            Toast(context).apply {
                duration = Toast.LENGTH_SHORT
                setGravity(Gravity.BOTTOM,0,0)
                view = layout
            }.show()

        }
    }

}