package com.example.taptopaysdk.presentation.model
import kotlinx.serialization.Serializable


@kotlinx.serialization.Serializable
data class SessionInfo(
    val expiresAt: Long = 0,
    val bundleId: String = "",
    val installationId: String = "",
    val merchantAccount: String = "",
    val clientKey: String = "",
    val userAgent: String = "",
    val mode: String = "",
    val version: Int = 0,
    val setupTokenPublicKeyHash: String = "",
    val device: DeviceInfo = DeviceInfo(),
    val library: LibraryInfo = LibraryInfo(),
    val publicKey: PublicKeyInfo = PublicKeyInfo()
)

@Serializable
data class DeviceInfo(
    val name: String = "",
    val model: String = "",
    val systemName: String = "",
    val systemVersion: String = ""
)

@Serializable
data class LibraryInfo(
    val name: String = "",
    val version: String = "",
    val buildType: String = ""
)

@Serializable
data class PublicKeyInfo(
    val kty: String = "",
    val crv: String = "",
    val x: String = "",
    val y: String = ""
)
