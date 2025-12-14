/*package com.example.taptopaysdk.presentation.ui.screens.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.taptopaysdk.data.storeSession.SessionRepository

import com.example.taptopaysdk.di.AppContainer

class SessionInfoViewModelFactory(
    private val appContext: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SessionInfoViewModel::class.java)) {
            return SessionInfoViewModel(
                repo = SessionRepository(appContext),
                clearSessionUseCase = AppContainer.performClearSessionUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}*/

package com.example.taptopaysdk.presentation.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.taptopaysdk.di.AppContainer

class SessionInfoViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SessionInfoViewModel::class.java)) {
            return SessionInfoViewModel(
                repo = AppContainer.sessionRepository,
                clearSessionUseCase = AppContainer.performClearSessionUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

