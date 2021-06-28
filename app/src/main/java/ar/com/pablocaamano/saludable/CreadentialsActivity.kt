package ar.com.pablocaamano.saludable

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import ar.com.pablocaamano.saludable.utils.ActivityUtils

class CreadentialsActivity : AppCompatActivity() {

    private val utils: ActivityUtils = ActivityUtils();

    private lateinit var user: EditText;
    private lateinit var pwd: EditText;
    private lateinit var pwd2: EditText;

    private lateinit var nextBtn: Button;
    private lateinit var backBtn: Button;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creadentials);

        this.initElements();
    }

    private fun initElements() {
        this.user = findViewById(R.id.credentials_et_user);
        this.pwd = findViewById(R.id.credentials_et_pwd);
        this.pwd2 = findViewById(R.id.credentials_et_pwd_repeat);

        this.nextBtn = findViewById(R.id.credentials_btn_ok);
        this.backBtn = findViewById(R.id.credentials_btn_back);
    }
}