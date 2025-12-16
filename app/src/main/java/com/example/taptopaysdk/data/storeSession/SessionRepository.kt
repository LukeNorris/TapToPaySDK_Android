/*package com.example.taptopaysdk.data.storeSession

import android.content.Context
import com.example.taptopaysdk.domain.session.SessionRepository
import com.example.taptopaysdk.presentation.model.SessionInfo
import kotlinx.coroutines.flow.Flow

class SessionRepository(private val context: Context) : SessionRepository {

    val sessionInfo: Flow<SessionInfo> =
        context.sessionInfoDataStore.data

    suspend fun saveSession(session: SessionInfo) {
        context.sessionInfoDataStore.updateData { session }
    }

    suspend fun clearSession() {
        context.sessionInfoDataStore.updateData { SessionInfo() }
    }
}*/

package com.example.taptopaysdk.data.storeSession

import android.content.Context
import com.adyen.ipp.api.InPersonPayments
import com.example.taptopaysdk.domain.session.SessionRepository
import com.example.taptopaysdk.presentation.model.SessionInfo
import kotlinx.coroutines.flow.Flow

class SessionRepositoryImpl(
    private val context: Context
) : SessionRepository {

    // ✅ implements domain contract
    override val sessionInfo: Flow<SessionInfo> =
        context.sessionInfoDataStore.data

    // ✅ data-only responsibility (used by AuthenticationProvider)
    suspend fun saveSession(session: SessionInfo) {
        context.sessionInfoDataStore.updateData { session }
    }

    // ✅ implements domain contract
    override suspend fun clearSession() {
        context.sessionInfoDataStore.updateData { SessionInfo() }
    }

    suspend fun getInstallationId(): Result<String> {
        return InPersonPayments.getInstallationId()
    }
}

