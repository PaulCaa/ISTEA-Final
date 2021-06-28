package ar.com.pablocaamano.saludable.model

import java.io.Serializable
import java.util.*

data class Patient(var name: String, var surname: String, var dni: Int, var gender: Char, var birthDate: String, var city: String, var user: String, var pwd: String, var treatment: String, var status: Boolean) : Serializable;
