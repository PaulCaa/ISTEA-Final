package ar.com.pablocaamano.saludable

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import ar.com.pablocaamano.saludable.model.DailyFood
import ar.com.pablocaamano.saludable.model.Report
import ar.com.pablocaamano.saludable.utils.ActivityUtils
import ar.com.pablocaamano.saludable.utils.FormUtils

class DayReportActivity : AppCompatActivity() {

    private val utils: ActivityUtils = ActivityUtils();
    private val formUtils: FormUtils = FormUtils();

    private lateinit var report: Report;
    private lateinit var food: DailyFood;

    private lateinit var title: TextView;
    private lateinit var mainFood: EditText;
    private lateinit var secondFood: EditText;
    private lateinit var drink: EditText;
    private lateinit var dessertTitle: TextView;
    private lateinit var dessertGroup: RadioGroup;
    private lateinit var dessertDescript: EditText;
    private lateinit var otherGroup: RadioGroup;
    private lateinit var otherDescript: EditText;
    private lateinit var hungerGroup: RadioGroup;

    private lateinit var cancel: Button;
    private lateinit var save: Button;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_report);

        this.initElements();

        this.save.setOnClickListener(View.OnClickListener {
            val res = this.createRegister();
            if(res) {
                utils.goToActivity(this,PreReportActivity::class.java,this.report);
            }
        });

        this.cancel.setOnClickListener(View.OnClickListener {
            utils.goToActivity(this,PreReportActivity::class.java,this.report.patient);
        });
    }

    private fun initElements() {
        this.title = findViewById(R.id.report_tv_title);
        this.mainFood = findViewById(R.id.report_et_mainfood);
        this.secondFood = findViewById(R.id.report_et_secondfood);
        this.drink = findViewById(R.id.report_et_drink);
        this.dessertTitle = findViewById(R.id.report_tv_dessert_subtitle);
        this.dessertGroup = findViewById(R.id.report_rg_dessert);
        this.dessertDescript = findViewById(R.id.report_et_dessert_descrip);
        this.otherGroup = findViewById(R.id.report_rg_other);
        this.otherDescript = findViewById(R.id.report_et_other_descrip);
        this.hungerGroup = findViewById(R.id.report_rg_hunger);

        this.save = findViewById(R.id.report_btn_save);
        this.cancel = findViewById(R.id.report_btn_cancel);

        // obtengo info de la activity anterior
        if(intent.getSerializableExtra("report") != null) {
            this.report = intent.getSerializableExtra("report") as Report;
            for(f in report.dailyFoods) {
                if(!f.charged) {
                    this.food = f
                    this.report.dailyFoods.remove(f);
                };
            }
            this.configForm(report.date);
        }
    }

    private fun configForm(date: String) {
        this.title.text = "${this.food.name} del $date";
        val f: String = this.food.name;
        if(f == "DESAYUNO" || f == "MERIENDA") {
            this.dessertTitle.visibility = View.GONE;
            this.dessertGroup.visibility = View.GONE;
            this.dessertDescript.visibility = View.GONE;
        }
    }

    fun rbClick(view: View) {
        if(view is RadioButton) {
            val checked = view.isChecked;
            when (view.getId()) {
                R.id.report_rb_dessert_y -> if (checked) this.dessertDescript.isEnabled = true;
                R.id.report_rb_dessert_n -> if (checked) this.dessertDescript.isEnabled = false;
                R.id.report_rb_other_y -> if (checked) this.otherDescript.isEnabled = true;
                R.id.report_rb_other_n -> if (checked) this.otherDescript.isEnabled = false;
            }
        }
    }

    private fun createRegister() : Boolean {
        var valid: Boolean = true;

        val main: String = this.mainFood.text.toString();
        if(formUtils.inputValidation(main)) {
            this.formUtils.markErrorInEditText(mainFood);
            valid = false;
        }

        val second: String = this.secondFood.text.toString();
        if(formUtils.inputValidation(second)) {
            this.formUtils.markErrorInEditText(secondFood);
            valid = false;
        }

        val drink: String = this.drink.text.toString();
        if(formUtils.inputValidation(drink)) {
            this.formUtils.markErrorInEditText(this.drink);
            valid = false;
        }

        var after: String = "";
        if(this.dessertGroup.checkedRadioButtonId == R.id.report_rb_dessert_y){
            after= this.dessertDescript.text.toString();
            if(formUtils.inputValidation(after)) {
                this.formUtils.markErrorInEditText(dessertDescript);
                valid = false;
            }
        }

        var tent: String = "";
        if(this.otherGroup.checkedRadioButtonId == R.id.report_rb_other_y){
            tent= this.otherDescript.text.toString();
            if(formUtils.inputValidation(tent)) {
                this.formUtils.markErrorInEditText(otherDescript);
                valid = false;
            }
        }

        var hu: Boolean = false;
        if(this.hungerGroup.checkedRadioButtonId == R.id.report_rb_hunger_y) hu = true;

        if(valid){
            this.food.mainFood = main;
            this.food.secondFood = second;
            this.food.drink = drink;
            this.food.afterFood = after;
            this.food.tentation = tent;
            this.food.satisfied = hu;
            this.food.charged = true;
            this.report.dailyFoods.add(this.food);
            // TODO insert o update en DB
        }

        return valid;
    }
}