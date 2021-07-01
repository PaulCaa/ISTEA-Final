package ar.com.pablocaamano.saludable.utils

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

/**
 * Clase para serializar y deserializar objetos
 * @author: Pablo Caamaño
 */
class ModelUtils {

    fun serialize(obj: Any) : ByteArray {
        val baos: ByteArrayOutputStream = ByteArrayOutputStream();
        val oos: ObjectOutputStream = ObjectOutputStream(baos);
        oos.writeObject(obj);
        oos.close();
        return baos.toByteArray();
    }

    fun deserialize(ba: ByteArray) : Any {
        val bais: ByteArrayInputStream = ByteArrayInputStream(ba);
        val ois: ObjectInputStream = ObjectInputStream(bais);
        return ois.readObject();
    }
}