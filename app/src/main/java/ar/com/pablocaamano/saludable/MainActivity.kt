package ar.com.pablocaamano.saludable

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import ar.com.pablocaamano.saludable.utils.ActivityUtils

class MainActivity : AppCompatActivity() {

    private val utils: ActivityUtils = ActivityUtils();

    private lateinit var usr: EditText;
    private lateinit var pwd: EditText;
    private lateinit var ok: Button;
    private lateinit var reg: Button;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initElements();


        reg.setOnClickListener(View.OnClickListener {
            utils.goToActivity(this,RegisterActivity::class.java);
        });
    }


    private fun initElements() {
        this.usr = findViewById(R.id.login_et_usr);
        this.pwd = findViewById(R.id.login_et_pwd);
        this.ok = findViewById(R.id.login_btn_ok);
        this.reg = findViewById(R.id.login_btn_register);
    }
}