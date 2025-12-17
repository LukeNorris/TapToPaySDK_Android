package com.example.taptopaysdk.data.pos.executor

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import com.adyen.ipp.api.InPersonPayments
import com.adyen.ipp.api.initialization.InitializationState
import com.adyen.ipp.api.payment.PaymentInterfaceType
import com.adyen.ipp.api.payment.TransactionRequest
import com.adyen.ipp.api.ui.MerchantUiParameters
import com.example.taptopaysdk.R
import com.example.taptopaysdk.data.pos.initializer.PosSdkInitializer
import com.example.taptopaysdk.data.pos.nexo.NexoPaymentBuilder
import com.example.taptopaysdk.data.pos.nexo.NexoPaymentParams

/**
 * Helper to:
 *  - ensure the SDK is initialized
 *  - build a NEXO payment request
 *  - start a Tap to Pay transaction UI.
 */
object TapToPayExecutor {

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun startPayment(
        activity: Activity,
        paymentLauncher: ActivityResultLauncher<Intent>,   // 👈 use Intent here
        amount: Double,
        currency: String = "EUR"
    ) {
        // 1) Make sure SDK is initialized
        val initState = PosSdkInitializer.initialize(activity)

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
                return
            }
        }

        // 2) Build the NEXO Terminal API payment request
        val poiId = InPersonPayments.getInstallationId()
            .getOrElse {
                Log.e("TapToPayExecutor", "Failed to get installation ID, using fallback", it)
                "AndroidSamplePOIID"
            }

        val params = NexoPaymentParams(
            amount = amount,
            currency = currency,
            paymentType = "Normal",
            saleReferenceId = "QuickPay-${System.currentTimeMillis()}"
        )

        val built = NexoPaymentBuilder.buildPaymentRequest(
            params = params,
            poiId = poiId
        )

        Log.d("TapToPayExecutor", "NEXO JSON:\n${built.json}")

        val transactionRequest = TransactionRequest.create(built.json)
            .getOrElse { exception ->
                Log.e("TapToPayExecutor", "Failed to create TransactionRequest from JSON", exception)
                // You can either throw or return early – here we just return to avoid crash
                return
            }

        // 3) Get payment interface for Tap to Pay
        val tapToPayInterfaceType = PaymentInterfaceType.createTapToPayType()

        val paymentInterfaceResult = InPersonPayments.getPaymentInterface(tapToPayInterfaceType)

        val paymentInterface = paymentInterfaceResult.getOrElse { exception ->
            Log.e("TapToPayExecutor", "Failed to get TapToPay payment interface", exception)
            return
        }

        // 4) Start the Tap to Pay UI
        InPersonPayments.performTransaction(
            context = activity,
            paymentLauncher = paymentLauncher,            // 👈 launcher<Intent>
            paymentInterface = paymentInterface,
            transactionRequest = transactionRequest,
            merchantUiParameters = MerchantUiParameters.create(
                merchantLogo = R.drawable.ic_launcher_foreground,
                tapToPayUiParameters = MerchantUiParameters
                    .TapToPayUiParameters
                    .create(
                        animation = MerchantUiParameters
                            .TapToPayUiParameters
                            .TapToPayAnimationType
                            .front(
                                MerchantUiParameters
                                    .TapToPayUiParameters
                                    .TapToPayAnimationType
                                    .Front
                                    .NfcFrontPosition.TopCenter
                            )
                    )
            )
        )
    }
}
