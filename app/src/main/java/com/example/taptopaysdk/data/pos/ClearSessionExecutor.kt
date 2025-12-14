package com.example.taptopaysdk.data.pos

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import com.adyen.ipp.api.InPersonPayments
import com.adyen.ipp.api.InPersonPaymentsTools
import com.adyen.ipp.api.initialization.InitializationState

/**
 * Helper to:
 *  - ensure the SDK is initialized
 *  - build a NEXO payment request
 *  - start a Tap to Pay transaction UI.
 */
object ClearSessionExecutor {

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun clearSession(
        activity: Activity,
        paymentLauncher: ActivityResultLauncher<Intent>

    ) {
        // 1) Make sure SDK is initialized
        val initState = InPersonPaymentsTools.initializeManually(activity)

        when (initState) {
            InitializationState.SuccessfulInitialization -> {
                Log.d("TapToPayExecutor", "SDK initialized OK")
            }
            is InitializationState.FailedInitialization -> {
                Log.e("TapToPayExecutor", "SDK init failed: ${initState.failureReasons}")
                return
            }
            else -> {
                Log.d("TapToPayExecutor", "SDK init state: $initState")
            }
        }

        // 2) Build the NEXO Terminal API payment request
        val poiId = InPersonPayments.getInstallationId()
            .getOrElse {
                Log.e("TapToPayExecutor", "Failed to get installation ID, using fallback", it)
                "AndroidSamplePOIID"
            }


        InPersonPayments.clearSession()

    }
}
