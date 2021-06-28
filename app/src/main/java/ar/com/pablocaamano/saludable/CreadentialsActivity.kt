package ar.com.pablocaamano.saludable

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import ar.com.pablocaamano.saludable.model.Patient
import ar.com.pablocaamano.saludable.utils.ActivityUtils
import ar.com.pablocaamano.saludable.utils.FormUtils
import ar.com.pablocaamano.saludable.utils.PwdUtils
import java.util.regex.Pattern

class CreadentialsActivity : AppCompatActivity() {

    private val utils: ActivityUtils = ActivityUtils();
    private val formUtils: FormUtils = FormUtils();
    private val pwdUtils: PwdUtils = PwdUtils();

    private lateinit var patient: Patient;

    private lateinit var user: EditText;
    private lateinit var pwd: EditText;
    private lateinit var pwd2: EditText;
    private lateinit var pwdValid: TextView;

    private lateinit var nextBtn: Button;
    private lateinit var backBtn: Button;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creadentials);

        this.initElements();

        nextBtn.setOnClickListener(View.OnClickListener {
            this.patient = this.validateCredentials(this.patient);
            if(patient.status) utils.goToActivity(this, TreatmentActivity::class.java);
        });

        backBtn.setOnClickListener(View.OnClickListener {
            utils.goToActivity(this,RegisterActivity::class.java,this.patient);
        });
    }

    private fun initElements() {
        this.user = findViewById(R.id.credentials_et_user);
        this.pwd = findViewById(R.id.credentials_et_pwd);
        this.pwd2 = findViewById(R.id.credentials_et_pwd_repeat);
        this.pwdValid = findViewById(R.id.credentials_tv_security_level);

        this.nextBtn = findViewById(R.id.credentials_btn_ok);
        this.backBtn = findViewById(R.id.credentials_btn_back);

        // se valida que vengan datos de la activity anterior
        if(intent.getSerializableExtra("patient") != null){
            this.patient =intent.getSerializableExtra("patient") as Patient;
            user.setText("${patient.surname.lowercase()}.${patient.name.lowercase()}");
        } else {
            utils.goToActivity(this, RegisterActivity::class.java);
        }

    }

    private fun validateCredentials(patient: Patient): Patient {
        var valid: Boolean = true;

        val userIn: String = user.text.toString();
        if(userIn.isEmpty()){
            this.formUtils.markErrorInEditText(user);
            Toast.makeText(this,"Ingrese usuario a generar", Toast.LENGTH_LONG).show();
            patient.status = false;
        }

        val pwdIn: String = this.pwd.text.toString();
        val pwdIn2: String = this.pwd2.text.toString();
        if(pwdIn.isBlank() || pwdIn2.isBlank()) {
            this.formUtils.markErrorInEditText(pwd);
            this.formUtils.markErrorInEditText(pwd2);
            Toast.makeText(this,"Ingrese contraseña!!", Toast.LENGTH_LONG).show();
            patient.status = false;
        } else if(pwdIn != pwdIn2) {
            this.formUtils.markErrorInEditText(pwd);
            this.formUtils.markErrorInEditText(pwd2);
            this.formUtils.printInTextView(pwdValid,"Las contraseñas ingresadas no coinciden", Color.RED);
            patient.status = false;
        } else if (!this.validPwdCondition(pwdIn)) {
            this.formUtils.printInTextView(pwdValid,"Las contraseña cumple la condiciones requeridas", Color.YELLOW);
            patient.status = false;
        } else {
            // TODO validar que el usuario no exista en la base de datos
            this.patient.user = userIn;
            this.patient.pwd = this.pwdUtils.encrypt(pwdIn);
        }

        return patient;
    }

    private fun validPwdCondition(pwd: String): Boolean {
        val condition: Pattern = Pattern.compile("[a-zA-Z0-9]+");
        return (pwd.length >= 5 && condition.matcher(pwd).matches());
    }
}