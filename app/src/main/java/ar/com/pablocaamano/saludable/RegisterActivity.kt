package ar.com.pablocaamano.saludable

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import ar.com.pablocaamano.saludable.utils.ActivityUtils

class RegisterActivity : AppCompatActivity() {

    private val utils: ActivityUtils = ActivityUtils();

    private lateinit var nextBtn: Button;
    private lateinit var backBtn: Button;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.initElements();

        backBtn.setOnClickListener(View.OnClickListener {
            utils.goToActivity(this,MainActivity::class.java);
        });
    }

    private fun initElements() {
        this.nextBtn = findViewById(R.id.reg_btn_ok);
        this.backBtn = findViewById(R.id.reg_btn_back);
    }
}