package ar.com.pablocaamano.saludable

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import ar.com.pablocaamano.saludable.dao.PatientsDBHelper
import ar.com.pablocaamano.saludable.model.Patient
import ar.com.pablocaamano.saludable.utils.ActivityUtils

class TreatmentActivity : AppCompatActivity() {

    private val utils: ActivityUtils = ActivityUtils();
    private val db: PatientsDBHelper = PatientsDBHelper(this,null);

    private lateinit var patient: Patient;

    private lateinit var subtitle: TextView;
    private lateinit var rgTreatments: RadioGroup;
    private lateinit var rbOther: RadioButton;
    private lateinit var otherInput: EditText;

    private lateinit var nextBtn: Button;
    private lateinit var backBtn: Button;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treatment);

        this.initElements();

        // se valida que se haya seleccionado "OTRO" para activar el EditText
        this.rgTreatments.setOnCheckedChangeListener { group, checkedId ->
            val rb: RadioButton = findViewById(checkedId);
            if(rb == rbOther){
                this.otherInput.isEnabled = true;
            } else {
                this.otherInput.isEnabled = true;
            }
        }

        this.backBtn.setOnClickListener(View.OnClickListener {
           utils.goToActivity(this, CredentialsActivity::class.java,this.patient);
        });

        this.nextBtn.setOnClickListener(View.OnClickListener {
            this.validateSelection();
            val registerResult = this.registerUser();
            if(registerResult){
                Toast.makeText(this,"Usuario creado, ya puede loguearse :)",Toast.LENGTH_SHORT).show();
                this.utils.goToActivity(this,MainActivity::class.java);
            } else {
                Toast.makeText(this,"Ocurrió un error al registrar usuario, intentelo de buevo",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private fun initElements() {
        this.subtitle = findViewById(R.id.treatment_tv_subtitle);

        this.rgTreatments = findViewById(R.id.treatment_rg_treatment);
        this.rbOther = findViewById(R.id.treatment_rb_other);
        this.otherInput = findViewById(R.id.treatment_et_other);

        this.nextBtn = findViewById(R.id.treatment_btn_ok);
        this.backBtn = findViewById(R.id.treatment_btn_back);

        if(intent.getSerializableExtra("patient") != null) {
            this.patient = intent.getSerializableExtra("patient") as Patient;
            val aux: String = subtitle.text.toString();
            subtitle.text = patient.name + aux;
        }  else {
            utils.goToActivity(this, CredentialsActivity::class.java);
        }
    }

    private fun validateSelection() {
        val selectedId: Int = this.rgTreatments.checkedRadioButtonId;
        val rb: RadioButton = findViewById(selectedId);
        this.patient.treatment = rb.text.toString();
    }

    private fun registerUser(): Boolean {
        return db.insertUser(this.patient);
    }
}