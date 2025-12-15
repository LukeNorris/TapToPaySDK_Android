
package com.example.taptopaysdk.di

import android.content.Context
import com.example.taptopaysdk.data.pos.ClearSessionExecutor
import com.example.taptopaysdk.data.pos.TapToPayExecutor
import com.example.taptopaysdk.data.transaction.TransactionRepositoryImpl
import com.example.taptopaysdk.data.storeSession.SessionRepositoryImpl
import com.example.taptopaysdk.domain.payment.PaymentResultHandler
import com.example.taptopaysdk.domain.repository.TransactionRepository
import com.example.taptopaysdk.domain.session.SessionRepository
import com.example.taptopaysdk.domain.usecase.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object AppContainer {

    // -------------------------
    // INITIALIZATION (REQUIRED)
    // -------------------------

    lateinit var sessionRepository: SessionRepository
        private set

    lateinit var transactionRepository: TransactionRepository
        private set

    lateinit var getTransactionsUseCase: GetTransactionsUseCase
        private set

    lateinit var recordTransactionUseCase: RecordTransactionUseCase
        private set

    lateinit var paymentResultHandler: PaymentResultHandler
        private set

    /**
     * Must be called ONCE from Application.onCreate()
     */
    fun init(appContext: Context) {

        // Session
        sessionRepository =
            SessionRepositoryImpl(appContext)

        // Transactions
        transactionRepository =
            TransactionRepositoryImpl(appContext)

        // Use cases
        getTransactionsUseCase =
            GetTransactionsUseCase(transactionRepository)

        recordTransactionUseCase =
            RecordTransactionUseCase(transactionRepository)

        // Payment handler
        paymentResultHandler =
            PaymentResultHandler(recordTransactionUseCase)
    }

    // -------------------------
    // PAYMENTS / SESSION
    // -------------------------

    val performTapToPayPaymentUseCase =
        PerformTapToPayPaymentUseCase(TapToPayExecutor)

    val performClearSessionUseCase =
        PerformClearSessionUseCase(ClearSessionExecutor)

    // -------------------------
    // TRANSACTION REFRESH BUS
    // -------------------------

    private val _transactionRefreshTrigger =
        MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    val transactionRefreshTrigger =
        _transactionRefreshTrigger.asSharedFlow()

    fun emitTransactionRefresh() {
        _transactionRefreshTrigger.tryEmit(Unit)
    }

    // Optional debug
    var sdkDataRaw: String? = null
}

