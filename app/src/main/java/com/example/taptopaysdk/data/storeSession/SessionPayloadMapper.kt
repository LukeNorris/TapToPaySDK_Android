package com.example.taptopaysdk.data.storeSession

import com.example.taptopaysdk.presentation.ui.model.DeviceInfo
import com.example.taptopaysdk.presentation.ui.model.LibraryInfo
import com.example.taptopaysdk.presentation.ui.model.PublicKeyInfo
import com.example.taptopaysdk.presentation.ui.model.SessionInfo
import org.json.JSONObject

object SessionPayloadMapper {

    fun toSessionInfo(payload: JSONObject): SessionInfo {

        val publicKeyJson = payload.optJSONObject("publicKey")
        val libraryJson = payload.optJSONObject("library")
        val deviceJson = payload.optJSONObject("device")

        return SessionInfo(
            expiresAt = payload.optLong("expiresAt", 0L),

            publicKey = PublicKeyInfo(
                x = publicKeyJson?.optString("x").orEmpty(),
                y = publicKeyJson?.optString("y").orEmpty(),
                kty = publicKeyJson?.optString("kty").orEmpty(),
                crv = publicKeyJson?.optString("crv").orEmpty()
            ),

            bundleId = payload.optString("bundleId"),
            installationId = payload.optString("installationId"),
            merchantAccount = payload.optString("merchantAccount"),
            clientKey = payload.optString("clientKey"),
            userAgent = payload.optString("userAgent"),

            library = LibraryInfo(
                name = libraryJson?.optString("name").orEmpty(),
                version = libraryJson?.optString("version").orEmpty(),
                buildType = libraryJson?.optString("buildType").orEmpty()
            ),

            device = DeviceInfo(
                systemName = deviceJson?.optString("systemName").orEmpty(),
                systemVersion = deviceJson?.optString("systemVersion").orEmpty(),
                model = deviceJson?.optString("model").orEmpty(),
                name = deviceJson?.optString("name").orEmpty(),
            ),

            mode = payload.optString("mode"),
            setupTokenPublicKeyHash = payload.optString("setupTokenPublicKeyHash"),
            version = payload.optInt("version", -1)
        )

    }

}
