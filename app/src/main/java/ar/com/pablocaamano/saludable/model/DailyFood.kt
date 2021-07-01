package ar.com.pablocaamano.saludable.model

import java.io.Serializable

data class DailyFood(val name: String, var mainFood: String, var drink: String, var afterFood: String, var satisfied: Boolean, var charged: Boolean): Serializable;