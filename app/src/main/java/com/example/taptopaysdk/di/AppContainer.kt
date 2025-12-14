package com.example.taptopaysdk.di

import android.content.Context
import com.example.taptopaysdk.data.pos.ClearSessionExecutor
import com.example.taptopaysdk.data.pos.TapToPayExecutor
import com.example.taptopaysdk.data.transaction.TransactionRepositoryImpl
//import com.example.taptopaysdk.data.storeSession.SessionRepository
import com.example.taptopaysdk.domain.session.SessionRepository
import com.example.taptopaysdk.data.storeSession.SessionRepositoryImpl
import com.example.taptopaysdk.domain.payment.PaymentResultHandler
import com.example.taptopaysdk.domain.repository.TransactionRepository
import com.example.taptopaysdk.domain.usecase.GetTransactionsUseCase
import com.example.taptopaysdk.domain.usecase.PerformClearSessionUseCase
import com.example.taptopaysdk.domain.usecase.PerformTapToPayPaymentUseCase
import com.example.taptopaysdk.domain.usecase.RecordTransactionUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object AppContainer {

    // -------------------------
    // INITIALIZATION (REQUIRED)
    // -------------------------

    /**
     * Must be called ONCE from Application.onCreate()
     */
    lateinit var sessionRepository: SessionRepository
        private set

    //fun init(appContext: Context) {
    //    sessionRepository = SessionRepository(appContext)
    //}
    fun init(appContext: Context) {
        sessionRepository = SessionRepositoryImpl(appContext)
    }

    // -------------------------
    // TRANSACTIONS
    // -------------------------

    private val transactionRepository: TransactionRepository =
        TransactionRepositoryImpl()

    val getTransactionsUseCase =
        GetTransactionsUseCase(transactionRepository)

    val recordTransactionUseCase =
        RecordTransactionUseCase(transactionRepository)

    // -------------------------
    // PAYMENTS / SESSION
    // -------------------------

    val performTapToPayPaymentUseCase =
        PerformTapToPayPaymentUseCase(TapToPayExecutor)

    val performClearSessionUseCase =
        PerformClearSessionUseCase(ClearSessionExecutor)

    // Optional: keep raw SDK data if useful for debugging
    var sdkDataRaw: String? = null

    // -------------------------
    // PAYMENT RESULT HANDLER
    // -------------------------

    val paymentResultHandler =
        PaymentResultHandler(recordTransactionUseCase)

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
