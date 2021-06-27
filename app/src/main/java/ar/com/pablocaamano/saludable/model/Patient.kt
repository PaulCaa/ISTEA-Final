package ar.com.pablocaamano.saludable.model

import java.io.Serializable
import java.util.*

data class Patient(val name: String, val surname: String, val dni: Int, val gender: Char, val dateBorn: Date, val city: String, val user: String, val pwd: String, val treatment: String) : Serializable;
