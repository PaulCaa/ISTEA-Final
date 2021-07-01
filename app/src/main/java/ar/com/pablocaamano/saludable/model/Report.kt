package ar.com.pablocaamano.saludable.model

import java.io.Serializable
import java.util.*

data class Report(var dni: Int, var date: Date, var dailyFoods: MutableList<DailyFood>) : Serializable;