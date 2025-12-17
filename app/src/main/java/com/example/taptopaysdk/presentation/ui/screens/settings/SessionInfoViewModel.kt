package com.example.taptopaysdk.presentation.ui.screens.settings

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
//import com.example.taptopaysdk.data.storeSession.SessionRepository
import com.example.taptopaysdk.domain.session.SessionRepository
import com.example.taptopaysdk.domain.usecase.ClearSessionUseCase
import com.example.taptopaysdk.presentation.ui.model.SessionInfo
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SessionInfoViewModel(
    private val repo: SessionRepository,
    private val clearSessionUseCase: ClearSessionUseCase
) : ViewModel() {

    val sessionInfo: StateFlow<SessionInfo?> =
        repo.sessionInfo
            .map { info ->
                // Treat "empty default" as no session
                if (info.installationId.isBlank()) null else info
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                null
            )

    fun clearSession(
        activity: ComponentActivity,
        paymentLauncher: ActivityResultLauncher<Intent>
    ) {
        viewModelScope.launch {
            try {
                // 1️⃣ Clear SDK session
                clearSessionUseCase(
                    activity = activity,
                    paymentLauncher = paymentLauncher
                )

                // 2️⃣ Clear stored session
                repo.clearSession()

            } catch (e: Exception) {
                // log if needed
            }
        }
    }
}


