package ar.com.pablocaamano.saludable

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import ar.com.pablocaamano.saludable.dao.PatientsDBHelper
import ar.com.pablocaamano.saludable.model.Patient
import ar.com.pablocaamano.saludable.utils.ActivityUtils
import ar.com.pablocaamano.saludable.utils.FormUtils
import ar.com.pablocaamano.saludable.utils.PwdUtils

class MainActivity : AppCompatActivity() {

    private val utils: ActivityUtils = ActivityUtils();
    private val formUtils: FormUtils = FormUtils();
    private val pwdUtils: PwdUtils = PwdUtils();
    private val  db: PatientsDBHelper = PatientsDBHelper(this,null);

    private lateinit var patient: Patient;

    private lateinit var usr: EditText;
    private lateinit var pwd: EditText;
    private lateinit var msg: TextView;
    private lateinit var ok: Button;
    private lateinit var reg: Button;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initElements();

        ok.setOnClickListener(View.OnClickListener {
            this.validateCredentials();
            if(this.patient.status) this.utils.goToActivity(this,PreReportActivity::class.java,this.patient);
        });

        reg.setOnClickListener(View.OnClickListener {
            utils.goToActivity(this,RegisterActivity::class.java);
        });
    }

    private fun initElements() {
        this.usr = findViewById(R.id.login_et_usr);
        this.pwd = findViewById(R.id.login_et_pwd);
        this.msg = findViewById(R.id.login_tv_message);

        this.ok = findViewById(R.id.login_btn_ok);
        this.reg = findViewById(R.id.login_btn_register);
    }

    private fun validateCredentials() {
        var valid: Boolean = true;

        val usrIn: String = usr.text.toString();
        if(usrIn.isBlank()){
            this.formUtils.markErrorInEditText(usr);
            valid = false;
            this.patient = Patient("","",0,'X',"","","","","",valid);
        }

        val pwdIn: String = pwd.text.toString();
        if(pwdIn.isBlank()){
            this.formUtils.markErrorInEditText(pwd);
            valid = false;
            this.patient = Patient("","",0,'X',"","","","","",valid);
        }

        if(valid){
            val res: List<Patient> = db.findUserByUsername(usrIn);
            if(res.isNotEmpty() && res[0].pwd == this.pwdUtils.encrypt(pwdIn) && res[0].user == usrIn)
                this.patient = Patient(res[0].name, res[0].surname, res[0].dni, res[0].gender, res[0].birthDate, res[0].city, res[0].user, res[0].pwd, res[0].treatment, true);
            else {
                this.printErrorCredentials("Usuario o contraseña erroneo");
            }
        } else {
            this.printErrorCredentials("Ingrese usuario y contraseña para acceder");
        }
    }

    private fun printErrorCredentials(msj: String){
        this.formUtils.markErrorInEditText(usr);
        this.formUtils.markErrorInEditText(pwd);
        this.msg.text = msj;
        this.patient = Patient("","",0,'X',"","","","","",false);

    }
}