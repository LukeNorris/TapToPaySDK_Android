package com.example.taptopaysdk

import android.app.Application
import com.example.taptopaysdk.di.AppContainer

class TapToPayApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize app-wide dependencies ONCE
        AppContainer.init(applicationContext)
    }
}
