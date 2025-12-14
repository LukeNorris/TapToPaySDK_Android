package com.example.taptopaysdk.data.storeSession

import android.util.Base64
import org.json.JSONObject

object JwtDecoder {

    /**
     * Decodes the *payload* (2nd segment) of a JWT (JWS compact).
     * Works for Base64URL (no padding).
     */
    fun decodeJwtPayload(jwt: String): JSONObject {
        val parts = jwt.split(".")
        require(parts.size >= 2) { "Invalid JWT: expected at least 2 parts, got ${parts.size}" }

        val payloadB64Url = parts[1]

        val jsonString = String(
            Base64.decode(payloadB64Url, Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING),
            Charsets.UTF_8
        )

        return JSONObject(jsonString)
    }

    /**
     * sdkData is a JWT. Its payload contains a nested JWT under "certificate".
     * This returns the nested certificate payload (the one you want to map to SessionInfo).
     */
    fun decodeCertificatePayloadFromSdkData(sdkData: String): JSONObject {
        val outerPayload = decodeJwtPayload(sdkData)
        val certificateJwt = outerPayload.optString("certificate")

        require(certificateJwt.isNotBlank()) {
            "sdkData payload missing 'certificate' field"
        }

        return decodeJwtPayload(certificateJwt)
    }
}
