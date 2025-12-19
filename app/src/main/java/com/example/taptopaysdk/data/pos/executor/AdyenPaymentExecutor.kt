package com.example.taptopaysdk.data.pos.executor

import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.activity.ComponentActivity
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
import com.example.taptopaysdk.domain.model.PaymentMethod
import com.example.taptopaysdk.domain.payment.PaymentExecutor

object AdyenPaymentExecutor : PaymentExecutor {

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun startPayment(
        activity: ComponentActivity,
        paymentLauncher: ActivityResultLauncher<Intent>,
        amount: Double,
        currency: String,
        method: PaymentMethod
    ) {
        val initState = PosSdkInitializer.initialize(activity)

        if (initState !is InitializationState.SuccessfulInitialization) {
            Log.e("AdyenPaymentExecutor", "SDK init failed: $initState")
            return
        }

        val poiId = InPersonPayments.getInstallationId()
            .getOrElse {
                Log.e("AdyenPaymentExecutor", "Failed to get installationId", it)
                return
            }

        val params = NexoPaymentParams(
            amount = amount,
            currency = currency,
            saleReferenceId = "QuickPay-${System.currentTimeMillis()}"
        )

        val built = NexoPaymentBuilder.buildPaymentRequest(params, poiId)

        val transactionRequest = TransactionRequest.create(built.json)
            .getOrElse {
                Log.e("AdyenPaymentExecutor", "Invalid NEXO JSON", it)
                return
            }

        val interfaceType = when (method) {
            PaymentMethod.TAP_TO_PAY ->
                PaymentInterfaceType.createTapToPayType()
            PaymentMethod.CARD_READER ->
                PaymentInterfaceType.createCardReaderType()
        }

        val paymentInterface = InPersonPayments
            .getPaymentInterface(interfaceType)
            .getOrElse {
                Log.e("AdyenPaymentExecutor", "Failed to get payment interface", it)
                return
            }

        InPersonPayments.performTransaction(
            context = activity,
            paymentLauncher = paymentLauncher,
            paymentInterface = paymentInterface,
            transactionRequest = transactionRequest,
            merchantUiParameters = MerchantUiParameters.create(
                merchantLogo = R.drawable.ic_launcher_foreground
            )
        )
    }
}
