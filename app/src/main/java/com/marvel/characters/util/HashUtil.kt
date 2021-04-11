package com.marvel.characters.util

import java.math.BigInteger
import java.nio.charset.Charset
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class HashUtil {
    @Throws(NoSuchAlgorithmException::class)
    private fun getSHA(input: String): ByteArray? {
        // Static getInstance method is called with hashing SHA
        val md: MessageDigest = MessageDigest.getInstance("SHA-256")

        // digest() method called
        // to calculate message digest of an input
        // and return array of byte
        return md.digest(input.toByteArray(Charset.forName("UTF-8")))
    }

    fun toMD5(s: String): String {
        try {
            // Create MD5 Hash
            val digest = MessageDigest.getInstance("MD5")
            digest.update(s.toByteArray())
            val messageDigest = digest.digest()
            // Create Hex String
            val hexString = StringBuilder()
            for (aMessageDigest in messageDigest) {
                var h = Integer.toHexString(0xFF and aMessageDigest.toInt())
                while (h.length < 2) h = "0$h"
                hexString.append(h)
            }
            return hexString.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return ""
    }

    fun toHexString(input: String): String? {
        val hash = getSHA(input)
        // Convert byte array into signum representation
        val number = BigInteger(1, hash)

        // Convert message digest into hex value
        val hexString = StringBuilder(number.toString(16))

        // Pad with leading zeros
        while (hexString.length < 32) {
            hexString.insert(0, '0')
        }
        return hexString.toString()
    }
}