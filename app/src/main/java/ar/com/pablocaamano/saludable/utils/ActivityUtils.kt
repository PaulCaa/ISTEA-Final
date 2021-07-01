package ar.com.pablocaamano.saludable.utils

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import ar.com.pablocaamano.saludable.model.Patient
import ar.com.pablocaamano.saludable.model.Report

class ActivityUtils {
    fun <T>goToActivity(context: Context, view: Class<T>) {
        this.goToActivity(context,view,null);
    }

    fun <T>goToActivity(context: Context, view: Class<T>, param: Patient?) {
        val intent: Intent = Intent(context,view);
        if(param != null) {
            intent.putExtra("patient", param);
        }
        startActivity(context,intent,null);
    }

    fun  <T>goToActivity(context: Context, view: Class<T>, param: Report) {
        val intent: Intent = Intent(context,view);
        if(param != null) {
            intent.putExtra("report", param);
        }
        startActivity(context,intent,null);
    }
}