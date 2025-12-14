package com.example.taptopaysdk.data.authentication

import com.example.taptopaysdk.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import android.util.Log


/**
 * Demo-only client that calls Adyen /checkout/possdk/v68/sessions
 * directly from the app. In real life this MUST live on your backend.
 */
class AdyenSessionApi(
    private val client: OkHttpClient = OkHttpClient()
) {

    private val apiKey: String = BuildConfig.ADYEN_API_KEY
    private val merchantAccount: String = BuildConfig.ADYEN_MERCHANT_ACCOUNT
    private val storeId: String = BuildConfig.STORE

    private val baseUrl =
        "https://checkout-test.adyen.com/checkout/possdk/v68/sessions"

    suspend fun createSession(setupToken: String): String = withContext(Dispatchers.IO) {
        if (apiKey.isBlank() || merchantAccount.isBlank()) {
            throw IllegalStateException("Adyen API key or merchantAccount not configured")
        }

        val json = JSONObject().apply {
            put("merchantAccount", merchantAccount)
            put("setupToken", setupToken)
            if (storeId.isNotBlank()) {
                put("store", storeId)
            }
        }.toString()

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val body = json.toRequestBody(mediaType)

        Log.d(
            "AdyenSessionApi",
            "Sessions request json=$json, merchant=$merchantAccount, store=$storeId"
        )

        val request = Request.Builder()
            .url(baseUrl)
            .addHeader("X-API-Key", apiKey)
            .addHeader("Content-Type", "application/json")
            .post(body)
            .build()

        client.newCall(request).execute().use { response ->
            val bodyString = response.body?.string().orEmpty()

            Log.d(
                "AdyenSessionApi",
                "Sessions response code=${response.code}, body=$bodyString"
            )

            if (!response.isSuccessful) {
                throw IOException("Adyen sessions HTTP ${response.code}: $bodyString")
            }

            val obj = JSONObject(bodyString)
            obj.getString("sdkData")
        }
    }


}
