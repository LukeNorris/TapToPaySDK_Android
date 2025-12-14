package com.example.taptopaysdk.data.storeSession

import androidx.datastore.core.Serializer
import com.example.taptopaysdk.presentation.model.SessionInfo
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object SessionInfoSerializer : Serializer<SessionInfo> {

    // IMPORTANT — ALWAYS SAFE DEFAULT
    override val defaultValue: SessionInfo = SessionInfo()

    // Use a safe JSON config
    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    override suspend fun readFrom(input: InputStream): SessionInfo {
        return try {
            val text = input.readBytes().decodeToString()

            if (text.isBlank()) return defaultValue

            json.decodeFromString(
                SessionInfo.serializer(),
                text
            )
        } catch (e: Exception) {
            // The file is corrupted → return default
            defaultValue
        }
    }

    override suspend fun writeTo(t: SessionInfo, output: OutputStream) {
        val jsonString = json.encodeToString(SessionInfo.serializer(), t)
        output.write(jsonString.encodeToByteArray())
    }
}
