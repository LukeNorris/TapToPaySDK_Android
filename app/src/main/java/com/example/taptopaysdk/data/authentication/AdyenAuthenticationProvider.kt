package com.example.taptopaysdk.data.authentication

import android.content.Context
import android.util.Log
import com.adyen.ipp.api.authentication.AuthenticationProvider
import com.adyen.ipp.api.authentication.AuthenticationResponse
import com.example.taptopaysdk.data.storeSession.JwtDecoder
import com.example.taptopaysdk.data.storeSession.SessionPayloadMapper
//import com.example.taptopaysdk.data.storeSession.SessionRepository
import com.example.taptopaysdk.data.storeSession.SessionRepositoryImpl


class AdyenAuthenticationProvider(
    private val appContext: Context,
    private val sessionApi: AdyenSessionApi = AdyenSessionApi()
) : AuthenticationProvider {

    override suspend fun authenticate(setupToken: String): Result<AuthenticationResponse> {
        return try {
            Log.d("AdyenAuthProvider", "authenticate() called with setupToken=$setupToken")

            val sdkData = sessionApi.createSession(setupToken)
            Log.d("AdyenAuthProvider", "Received sdkData length=${sdkData.length}")
            Log.d("AdyenAuthProvider", "Received sdkData=$sdkData")

            // ✅ Decode the nested JWT payload (certificate), NOT the outer sdkData payload
            val certificatePayloadJson = JwtDecoder.decodeCertificatePayloadFromSdkData(sdkData)

            // Helpful debug:
            Log.d("AdyenAuthProvider", "Certificate payload keys=${certificatePayloadJson.keys().asSequence().toList()}")

            val sessionInfo = SessionPayloadMapper.toSessionInfo(certificatePayloadJson)
            Log.d("AdyenAuthProvider", "expiresAt=${certificatePayloadJson.optLong("expiresAt")}")
            Log.d("AdyenAuthProvider", "installationId=${certificatePayloadJson.optString("installationId")}")
            Log.d("AdyenAuthProvider", "device=${certificatePayloadJson.optJSONObject("device")}")


            // Store
            //SessionRepository(appContext).saveSession(sessionInfo)
            SessionRepositoryImpl(appContext).saveSession(sessionInfo)


            val authResponse = AuthenticationResponse.create(sdkData)
            Result.success(authResponse)

        } catch (e: Exception) {
            Log.e("AdyenAuthProvider", "Error in authenticate()", e)
            Result.failure(e)
        }
    }

}
