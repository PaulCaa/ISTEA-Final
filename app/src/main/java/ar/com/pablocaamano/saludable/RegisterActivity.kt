package ar.com.pablocaamano.saludable

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import ar.com.pablocaamano.saludable.fragments.DatePickerFragment
import ar.com.pablocaamano.saludable.model.Patient
import ar.com.pablocaamano.saludable.utils.ActivityUtils
import ar.com.pablocaamano.saludable.utils.FormUtils
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private val utils: ActivityUtils = ActivityUtils();
    private val formUtils: FormUtils = FormUtils();

    private lateinit var patient: Patient;

    private lateinit var nextBtn: Button;
    private lateinit var backBtn: Button;
    private lateinit var nameText: EditText;
    private lateinit var surnameText: EditText
    private lateinit var dni: EditText;
    private lateinit var genderTitle: TextView;
    private lateinit var genderGroup: RadioGroup;
    private lateinit var fGender: RadioButton;
    private lateinit var mGender: RadioButton;
    private lateinit var birthDate: EditText;
    private lateinit var location: EditText;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.initElements();

        birthDate.setOnClickListener(View.OnClickListener {
            this.showDatePicker();
        });

        backBtn.setOnClickListener(View.OnClickListener {
            utils.goToActivity(this,MainActivity::class.java);
        });

        nextBtn.setOnClickListener(View.OnClickListener {
            val p: Patient = this.createPatient();
            if(p.status) utils.goToActivity(this,CreadentialsActivity::class.java,p);
        });
    }

    private fun initElements() {
        this.nameText = findViewById(R.id.reg_et_name);
        this.surnameText = findViewById(R.id.reg_et_surname);
        this.dni = findViewById(R.id.reg_et_dni);
        this.genderTitle = findViewById(R.id.reg_tv_gender_title);
        this.genderGroup = findViewById(R.id.reg_rg_gender);
        this.fGender = findViewById(R.id.reg_rb_female);
        this.mGender = findViewById(R.id.reg_rb_male);
        this.birthDate = findViewById(R.id.reg_et_born);
        this.location = findViewById(R.id.reg_et_locale);

        this.nextBtn = findViewById(R.id.reg_btn_ok);
        this.backBtn = findViewById(R.id.reg_btn_back);

        if(intent.getSerializableExtra("patient") != null){
            this.patient =intent.getSerializableExtra("patient") as Patient;
            nameText.setText(patient.name);
            surnameText.setText(patient.surname);
            dni.setText(patient.dni);
            birthDate.setText(patient.birthDate);
            location.setText(patient.city);
        }
    }

    private fun showDatePicker(){
        val datePicker = DatePickerFragment { day, month, year -> dateSelected(day, month, year) }
        datePicker.show(supportFragmentManager, "datePicker");
    }

    private fun dateSelected(day: Int, month: Int, year: Int) {
        birthDate.setText("$day/$month/$year");
    }

    private fun createPatient(): Patient {
        var valid: Boolean = true;

        val nameIn: String = this.nameText.text.toString();
        if (this.emptyInputValidation(nameIn)) {
            this.formUtils.markErrorInEditText(nameText);
            valid = false;
        }
        val surnameIn: String = this.surnameText.text.toString();
        if (this.emptyInputValidation(surnameIn)) {
            this.formUtils.markErrorInEditText(surnameText);
            valid = false;
        }
        val dniIn: String = this.dni.text.toString();
        if (this.emptyInputValidation(dniIn) || dniIn.length < 5) {
            this.formUtils.markErrorInEditText(dni);
            valid = false;
        }

        val dateIn: String = this.birthDate.text.toString();
        if(this.emptyInputValidation(dateIn)) {
            this.formUtils.markErrorInEditText(birthDate);
            valid = false
        }

        val locIn: String = this.location.text.toString();
        if (this.emptyInputValidation(locIn)) {
            this.formUtils.markErrorInEditText(location);
            valid = false;
        }

        var genderSelect: Char;
        val selectionId: Int = this.genderGroup.checkedRadioButtonId;
        if(selectionId == -1){
            this.genderTitle.setTextColor(Color.RED);
            this.mGender.setTextColor(Color.RED);
            this.fGender.setTextColor(Color.RED);
            valid = false;
        } else {
            if (selectionId == this.fGender.id) genderSelect = 'F';
            if (selectionId == this.mGender.id) genderSelect = 'M';
        }


        return if (valid) {
            Patient(nameIn, surnameIn, Integer.valueOf(dniIn), 'F', dateIn, locIn, "", "", "", valid);
        } else {
            Toast.makeText(this, "Faltan datos!", Toast.LENGTH_LONG).show();
            Patient("", "", 0, 'F', "", "", "", "", "", valid);
        }
    }

    private fun emptyInputValidation(param: String) : Boolean {
        if(param.isEmpty() || param.isBlank())
            return true;
        return false;
    }
}