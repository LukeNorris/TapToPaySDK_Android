package com.example.taptopaysdk.data.authentication

import android.annotation.SuppressLint
import com.adyen.ipp.api.authentication.AuthenticationProvider
import com.adyen.ipp.api.authentication.MerchantAuthenticationService

class AdyenAuthenticationService : MerchantAuthenticationService() {

    override lateinit var authenticationProvider: AuthenticationProvider

    @SuppressLint("RestrictedApi") // SDK requires calling super.onCreate()
    override fun onCreate() {
        super.onCreate()
        authenticationProvider = AdyenAuthenticationProvider(appContext = this)
    }
}