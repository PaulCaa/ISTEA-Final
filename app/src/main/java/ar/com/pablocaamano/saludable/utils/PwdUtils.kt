package ar.com.pablocaamano.saludable.utils

import java.math.BigInteger
import java.security.MessageDigest

class PwdUtils {

    fun encrypt(pwd: String): String {
        val md5: MessageDigest = MessageDigest.getInstance("MD5");
        return BigInteger(1,
            md5.digest(pwd.toByteArray())).toString(16).padStart(32,
            '0');
    }
}