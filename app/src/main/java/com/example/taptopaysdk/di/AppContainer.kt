package com.example.taptopaysdk.di

import android.content.Context
import android.util.Log
import com.example.taptopaysdk.data.pos.ClearSessionExecutor
import com.example.taptopaysdk.data.pos.ReversalRepositoryImpl
import com.example.taptopaysdk.data.pos.TapToPayExecutor
import com.example.taptopaysdk.data.transaction.TransactionRepositoryImpl
import com.example.taptopaysdk.data.storeSession.SessionRepositoryImpl
import com.example.taptopaysdk.domain.payment.PaymentResultHandler
import com.example.taptopaysdk.domain.repository.ReversalRepository
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

    lateinit var reversalRepository: ReversalRepository
        private set

    lateinit var getTransactionsUseCase: GetTransactionsUseCase
        private set

    lateinit var recordTransactionUseCase: RecordTransactionUseCase
        private set

    lateinit var refundTransactionUseCase: RefundTransactionUseCase
        private set

    lateinit var paymentResultHandler: PaymentResultHandler
        private set

    /**
     * Must be called ONCE from Application.onCreate()
     */
    fun init(appContext: Context) {

        Log.d("AppContainer", "Initializing AppContainer")

        // -------------------------
        // Session
        // -------------------------
        sessionRepository =
            SessionRepositoryImpl(appContext)

        // -------------------------
        // Transactions
        // -------------------------
        transactionRepository =
            TransactionRepositoryImpl(appContext)

        // -------------------------
        // Reversal (Refunds)
        // -------------------------
        reversalRepository =
            ReversalRepositoryImpl()

        // -------------------------
        // Use cases
        // -------------------------
        getTransactionsUseCase =
            GetTransactionsUseCase(transactionRepository)

        recordTransactionUseCase =
            RecordTransactionUseCase(transactionRepository)

        refundTransactionUseCase =
            RefundTransactionUseCase(reversalRepository)

        // -------------------------
        // Payment result handler
        // -------------------------
        paymentResultHandler =
            PaymentResultHandler(recordTransactionUseCase)

        Log.d("AppContainer", "AppContainer initialized successfully")
    }

    //--------------------------
    // PAYMENT COMPLETED
    //--------------------------
    private val _paymentCompletedTrigger =
        MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    val paymentCompletedTrigger =
        _paymentCompletedTrigger.asSharedFlow()

    fun emitPaymentCompleted() {
        _paymentCompletedTrigger.tryEmit(Unit)
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

}
