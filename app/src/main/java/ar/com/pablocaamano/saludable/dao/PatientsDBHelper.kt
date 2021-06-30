package ar.com.pablocaamano.saludable.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import ar.com.pablocaamano.saludable.model.Patient
import java.lang.Exception

class PatientsDBHelper (context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context,DATABASE_NAME,factory,DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "patients-v1.db";
        private const val DATABASE_VERSION = 1;

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
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE = (
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
        db?.execSQL(CREATE_TABLE);
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if(oldVersion != newVersion) {
            db?.execSQL("DROP TABLE IF EXISTS $TABLE_PATIENTS");
            onCreate(db);
        }
    }


    fun insert(p: Patient) : Boolean {
        Log.i("PatientsDBHelper","registrando paciente = {'user': '${p.user}', 'dni': '${p.dni}', 'name': '${p.name}', 'surname': '${p.surname}'}");
        var result: Boolean;
        try{
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
            result = true;
        } catch(e: Exception) {
            Log.e("PatientsDBHelper",e.message.toString());
            result = false;
        }
        return result;
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
}