package ar.com.pablocaamano.saludable.utils

import android.content.res.ColorStateList
import android.graphics.Color
import android.widget.EditText
import android.widget.TextView

class FormUtils {

    fun markErrorInEditText(elem: EditText) {
        val color: ColorStateList = ColorStateList.valueOf(Color.RED);
        elem.backgroundTintList = color;

        elem.setHintTextColor(Color.RED);
        elem.setTextColor(Color.RED);
    }

    fun printInTextView(elem: TextView, msg: String, color: Int) {
        elem.setTextColor(color);
        elem.text = msg;
    }
}