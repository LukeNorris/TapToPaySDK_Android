package com.example.taptopaysdk.domain.usecase

import com.example.taptopaysdk.domain.session.SessionRepository
import com.example.taptopaysdk.presentation.ui.model.SessionInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetSessionUseCase(
    private val repository: SessionRepository
) {

    operator fun invoke(): Flow<SessionInfo?> {
        return repository.sessionInfo.map { info ->
            // Domain rule:
            // an empty/default session means "no active session"
            if (info.installationId.isBlank()) null else info
        }
    }
}
