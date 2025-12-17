package com.example.taptopaysdk.data.pos.initializer

import android.content.Context
import android.util.Log
import com.adyen.ipp.api.InPersonPaymentsTools
import com.adyen.ipp.api.initialization.InitializationState

/**
 * Small helper around Adyen InPersonPaymentsTools initialization.
 * For SDK 2.9.0:
 *  - initializeManually(context)      → requires Context
 *  - getInitializationState()        → no arguments
 */
object PosSdkInitializer {

    suspend fun initialize(context: Context): InitializationState {
        val state = InPersonPaymentsTools.initializeManually(context)

        when (state) {
            InitializationState.SuccessfulInitialization -> {
                Log.d("PosSdkInitializer", "SDK initialized successfully")
            }

            is InitializationState.FailedInitialization -> {
                Log.e(
                    "PosSdkInitializer",
                    "SDK initialization failed: ${state.failureReasons}"
                )
            }

            else -> {
                Log.d("PosSdkInitializer", "SDK initialization state: $state")
            }
        }

        return state
    }

    suspend fun currentState(): InitializationState {
        return InPersonPaymentsTools.getInitializationState()
    }
}
