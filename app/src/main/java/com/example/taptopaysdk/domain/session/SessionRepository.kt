package com.example.taptopaysdk.domain.session

import com.example.taptopaysdk.presentation.model.SessionInfo
import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    val sessionInfo: Flow<SessionInfo>

    suspend fun clearSession()
}