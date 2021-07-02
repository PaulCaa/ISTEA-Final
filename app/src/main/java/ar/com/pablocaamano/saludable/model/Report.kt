package ar.com.pablocaamano.saludable.model

import java.io.Serializable
import java.util.*

data class Report(var patient: Patient, var date: String, var dailyFoods: MutableList<DailyFood>) : Serializable;