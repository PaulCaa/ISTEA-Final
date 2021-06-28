package ar.com.pablocaamano.saludable

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import ar.com.pablocaamano.saludable.utils.ActivityUtils

class TreatmentActivity : AppCompatActivity() {

    private val utils: ActivityUtils = ActivityUtils();

    private lateinit var nextBtn: Button;
    private lateinit var backBtn: Button;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treatment);

        this.initElements();
    }

    private fun initElements() {
        this.nextBtn = findViewById(R.id.treatment_btn_ok);
        this.backBtn = findViewById(R.id.treatment_btn_back);
    }
}