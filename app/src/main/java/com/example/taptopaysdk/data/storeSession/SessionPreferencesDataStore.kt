package com.example.taptopaysdk.data.storeSession

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.example.taptopaysdk.presentation.ui.model.SessionInfo

private const val SESSION_INFO_FILE = "session_info.json"

val Context.sessionInfoDataStore: DataStore<SessionInfo> by dataStore(
    fileName = SESSION_INFO_FILE,
    serializer = SessionInfoSerializer
)
