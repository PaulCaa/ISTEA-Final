package ar.com.pablocaamano.saludable

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import ar.com.pablocaamano.saludable.dao.PatientsDBHelper
import ar.com.pablocaamano.saludable.model.DailyFood
import ar.com.pablocaamano.saludable.model.Patient
import ar.com.pablocaamano.saludable.model.Report
import ar.com.pablocaamano.saludable.utils.ActivityUtils
import java.text.SimpleDateFormat
import java.util.*

class PreReportActivity : AppCompatActivity() {

    private val utils: ActivityUtils = ActivityUtils();
    private val db: PatientsDBHelper = PatientsDBHelper(this, null);

    private lateinit var patient: Patient;
    private lateinit var report: Report;

    private lateinit var titleTextView: TextView;
    private lateinit var dateText: TextView;
    private lateinit var descriptText: TextView;

    private lateinit var breakfastBtn: Button;
    private lateinit var lunchBtn: Button;
    private lateinit var snackBtn: Button;
    private lateinit var dinnerBtn: Button;
    private lateinit var exitBtn: Button;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_report);

        this.initElements();

        this.breakfastBtn.setOnClickListener(View.OnClickListener {
            val df: DailyFood = DailyFood(FOOD_BREAKFAST,"","","",true, false);
            this.report.dailyFoods.add(df);
            utils.goToActivity(this,DayReportActivity::class.java,report);
        });

        this.lunchBtn.setOnClickListener(View.OnClickListener {
            val df: DailyFood = DailyFood(FOOD_LUNCH,"","","",true, false);
            this.report.dailyFoods.add(df);
            utils.goToActivity(this,DayReportActivity::class.java,report);
        });

        this.snackBtn.setOnClickListener(View.OnClickListener {
            val df: DailyFood = DailyFood(FOOD_SNACK,"","","",true, false);
            this.report.dailyFoods.add(df);
            utils.goToActivity(this,DayReportActivity::class.java,report);
        });

        this.dinnerBtn.setOnClickListener(View.OnClickListener {
            val df: DailyFood = DailyFood(FOOD_DINNER,"","","",true, false);
            this.report.dailyFoods.add(df);
            utils.goToActivity(this,DayReportActivity::class.java,report);
        });

        this.exitBtn.setOnClickListener(View.OnClickListener {
            utils.goToActivity(this,MainActivity::class.java);
        });
    }

    private fun initElements() {
        this.titleTextView = findViewById(R.id.pre_tv_title);
        this.dateText = findViewById(R.id.pre_tv_date);
        this.descriptText = findViewById(R.id.pre_tv_descript);

        this.breakfastBtn = findViewById(R.id.pre_btn_breakfast);
        this.lunchBtn = findViewById(R.id.pre_btn_lunch);
        this.snackBtn = findViewById(R.id.pre_btn_snack);
        this.dinnerBtn = findViewById(R.id.pre_btn_dinner);
        this.exitBtn = findViewById(R.id.pre_btn_exit);

        if(intent.getSerializableExtra("patient") != null) {
            this.patient = intent.getSerializableExtra("patient") as Patient;

            this.checkUserResgister();
        } else {
            Toast.makeText(this,"Error al acceder a los datos de usuario, vuelva a intentarlo", Toast.LENGTH_SHORT).show();
            utils.goToActivity(this,MainActivity::class.java);
        }
    }

    private fun checkUserResgister() {
        if(this.patient.gender == 'M') this.titleTextView.text = "Bienvenido ${this.patient.name}";
        else this.titleTextView.text = "Bienvenida ${this.patient.name}";

        val sdf: SimpleDateFormat = SimpleDateFormat("dd/M/yyyy");
        this.dateText.text = "Registro de: ${sdf.format(Date())}";


        // consultar reporte registrado en DB

        // si no hay registro se crea de cero
        val dailyFoods: MutableList<DailyFood> = mutableListOf<DailyFood>();
        this.report = Report(patient.dni,Date(),dailyFoods);
    }

    companion object {
        const val FOOD_BREAKFAST: String = "DESAYUNO";
        const val FOOD_LUNCH: String = "ALMUERZO";
        const val FOOD_SNACK: String = "MERIENDA";
        const val FOOD_DINNER: String = "CENA";
    }
}