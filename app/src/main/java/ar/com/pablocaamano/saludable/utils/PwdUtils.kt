package ar.com.pablocaamano.saludable.utils

import java.security.MessageDigest

class PwdUtils {

    fun encrypt(pwd: String): String {
        val md5: MessageDigest = MessageDigest.getInstance("MD5");
        val pwdBA: ByteArray = md5.digest(pwd.toByteArray());
        val sb : StringBuffer = StringBuffer();
        for (b in sb) {
            val i :Int = b.code and 0xff;
            var hexString = Integer.toHexString(i);
            if (hexString.length < 2)  hexString = "0$hexString"
            sb.append(hexString)
        }
        return sb.toString();
    }
}