package ar.com.pablocaamano.saludable.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import ar.com.pablocaamano.saludable.model.DailyFood
import ar.com.pablocaamano.saludable.model.Report
import ar.com.pablocaamano.saludable.model.Patient
import ar.com.pablocaamano.saludable.utils.ModelUtils
import java.lang.Exception

class PatientsDBHelper (context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context,DATABASE_NAME,factory,DATABASE_VERSION) {

    companion object {
        private val utils: ModelUtils = ModelUtils();

        private const val DATABASE_NAME = "patients-v2.db";
        private const val DATABASE_VERSION = 2;

        const val TABLE_PATIENTS = "patients";
        const val COLUMN_ID = "dni";
        const val COLUMN_NAME = "name";
        const val COLUMN_SURNAME = "surname";
        const val COLUMN_GENDER = "gender";
        const val COLUMN_BIRTHDATE = "birthdate";
        const val COLUMN_LOCATION = "location";
        const val COLUMN_USERNAME = "username";
        const val COLUMN_PWD = "pwd";
        const val COLUMN_TREATMENT = "treatment";

        const val TABLE_REPORTS = "reports";
        const val COLUMN_ID_REPORTS = "id_report";
        const val COLUMN_DNI = "dni";
        const val COLUMN_DATE = "date";
        const val COLUMN_REPORT_DETAIL = "report_detail";
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE_PATIENTS = (
                "CREATE TABLE [$TABLE_PATIENTS] (" +
                        "$COLUMN_ID INTEGER PRIMARY KEY," +
                        "$COLUMN_NAME TEXT," +
                        "$COLUMN_SURNAME TEXT," +
                        "$COLUMN_GENDER TEXT," +
                        "$COLUMN_BIRTHDATE TEXT," +
                        "$COLUMN_LOCATION TEXT," +
                        "$COLUMN_USERNAME TEXT," +
                        "$COLUMN_PWD TEXT," +
                        "$COLUMN_TREATMENT TEXT)"
                );
        db?.execSQL(CREATE_TABLE_PATIENTS);

        val CREATE_TABLE_REPORTS = (
                "CREATE TABLE [$TABLE_REPORTS] (" +
                        "$COLUMN_ID_REPORTS INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "$COLUMN_DNI INTEGER," +
                        "$COLUMN_DATE TEXT," +
                        "$COLUMN_REPORT_DETAIL BLOB)"
                );
        db?.execSQL(CREATE_TABLE_REPORTS);
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if(oldVersion != newVersion) {
            db?.execSQL("DROP TABLE IF EXISTS $TABLE_PATIENTS");
            db?.execSQL("DROP TABLE IF EXISTS $TABLE_REPORTS");
            onCreate(db);
        }
    }


    fun insertUser(p: Patient) : Boolean {
        Log.i("PatientsDBHelper","registrando paciente = {'user': '${p.user}', 'dni': '${p.dni}', 'name': '${p.name}', 'surname': '${p.surname}'}");
        return try{
            val db = this.writableDatabase;

            val values = ContentValues();
            values.put(COLUMN_ID,p.dni);
            values.put(COLUMN_NAME,p.name);
            values.put(COLUMN_SURNAME,p.surname);
            values.put(COLUMN_GENDER,p.gender.toString());
            values.put(COLUMN_BIRTHDATE,p.birthDate);
            values.put(COLUMN_LOCATION,p.city);
            values.put(COLUMN_USERNAME,p.user);
            values.put(COLUMN_PWD,p.pwd);
            values.put(COLUMN_TREATMENT,p.treatment);

            db.insert(TABLE_PATIENTS,null, values);
            Log.i("PatientsDBHelper","usuario registrado");
            true;
        } catch(e: Exception) {
            Log.e("PatientsDBHelper",e.message.toString());
            false;
        }
    }

    fun findUserByDocument(dni: Int) : List<Patient> {
        Log.i("PatientsDBHelper","buscando usuario con dni: $dni");
        val query = "SELECT ${COLUMN_ID},${COLUMN_NAME},${COLUMN_SURNAME},${COLUMN_GENDER},${COLUMN_BIRTHDATE},${COLUMN_LOCATION},${COLUMN_USERNAME},${COLUMN_PWD},${COLUMN_TREATMENT}" +
                " FROM $TABLE_PATIENTS WHERE $COLUMN_ID = $dni";
        return this.execSelect(query);
    }

    fun findUserByUsername(username: String) : List<Patient> {
        Log.i("PatientsDBHelper","buscando usuario: $username");
        val query = "SELECT ${COLUMN_ID},${COLUMN_NAME},${COLUMN_SURNAME},${COLUMN_GENDER},${COLUMN_BIRTHDATE},${COLUMN_LOCATION},${COLUMN_USERNAME},${COLUMN_PWD},${COLUMN_TREATMENT}" +
                " FROM $TABLE_PATIENTS WHERE $COLUMN_USERNAME = '$username'";
        return this.execSelect(query);
    }

    private fun execSelect(query: String) : List<Patient> {
        val result: MutableList<Patient> = mutableListOf();
        try {
            val db = this.writableDatabase;
            val cursor: Cursor = db.rawQuery(query, null);
            if(cursor.moveToFirst()) {
                val resDNI = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                val resName = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                val resSurname = cursor.getString(cursor.getColumnIndex(COLUMN_SURNAME));
                val resGender = cursor.getString(cursor.getColumnIndex(COLUMN_GENDER));
                val resBirthdate = cursor.getString(cursor.getColumnIndex(COLUMN_BIRTHDATE));
                val resLocation = cursor.getString(cursor.getColumnIndex(COLUMN_LOCATION));
                val resUsername = cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME));
                val resPwd = cursor.getString(cursor.getColumnIndex(COLUMN_PWD));
                val resTreatment = cursor.getString(cursor.getColumnIndex(COLUMN_TREATMENT));
                result.add(Patient(resName,resSurname,resDNI,resGender[0],resBirthdate,resLocation,resUsername,resPwd,resTreatment,true));
            }
            Log.i("PatientsDBHelper", "se obtuvieron ${result.size} registros");
        } catch (e: Exception) {
            Log.e("PatientsDBHelper",e.message.toString());
        }
        return result;
    }

    fun insertReport(r: Report) : Boolean {
        Log.i("PatientsDBHelper","registrando reporte del paciente DNI: '${r.patient.dni}', del ${r.date}");
        return try {
            val db = this.writableDatabase;

            val values = ContentValues();
            values.put(COLUMN_DNI, r.patient.dni);
            values.put(COLUMN_DATE, r.date.toString());
            values.put(COLUMN_REPORT_DETAIL, utils.serialize(r.dailyFoods));

            db.insert(TABLE_REPORTS, null, values);
            Log.i("PatientsDBHelper","reporte registrado");
            true;
        } catch(e: Exception) {
            Log.e("PatientsDBHelper",e.message.toString());
            false;
        }
    }

    fun findReportByPatient(p: Patient, date: String): Report {
        Log.i("PatientsDBHelper","buscando reportes del DNI ${p.dni}");
        val result: Report = Report(p,date, mutableListOf());
        val query = "SELECT $COLUMN_REPORT_DETAIL " +
                "FROM $TABLE_REPORTS " +
                "WHERE $COLUMN_DNI = ${p.dni} " +
                "AND $COLUMN_DATE = '$date'";
        try{
            val db = this.writableDatabase;
            val cursor: Cursor = db.rawQuery(query, null);
            if(cursor.moveToFirst()) {
                val resFoods: ByteArray = cursor.getBlob(cursor.getColumnIndex(COLUMN_REPORT_DETAIL));
                result.dailyFoods = utils.deserialize(resFoods) as MutableList<DailyFood>;
            }
            Log.i("PatientsDBHelper", "se obtuvieron ${result.dailyFoods.size} reportes");
        } catch (e: Exception) {
            Log.e("PatientsDBHelper",e.message.toString());
        }
        return result;
    }
}