package com.silver.github.util

import android.widget.Toast
import com.silver.github.App

class ToastUtil {

    companion object {

        fun s(resId: Int) {
            Toast.makeText(App.getContext(), resId, Toast.LENGTH_SHORT).show()
        }

        fun s(text: String) {
            Toast.makeText(App.getContext(), text, Toast.LENGTH_SHORT).show()
        }

        fun l(resId: Int) {
            Toast.makeText(App.getContext(), resId, Toast.LENGTH_LONG).show()
        }

        fun l(message: String) {
            Toast.makeText(App.getContext(), message, Toast.LENGTH_LONG).show()
        }
    }

}