package com.jedev.grpc.kt.helper

import com.github.ajalt.timberkt.*
import io.grpc.*
import io.grpc.kotlin.AbstractCoroutineStub
import io.grpc.stub.MetadataUtils

private fun Metadata.put(key: String, value: String) {
    val metaKey = Metadata.Key.of(key, Metadata.ASCII_STRING_MARSHALLER)

    return put(metaKey, value)
}

fun <T : AbstractCoroutineStub<T>> T.addMetaData(key: String, value: String): T = withInterceptors(
    MetadataUtils.newAttachHeadersInterceptor(Metadata().apply {
        put(key, value)
    })
)

fun <T : AbstractCoroutineStub<T>> T.addOption(
    key: String, value: String
): T = withOption(CallOptions.Key.create(key), value)

fun <T : AbstractCoroutineStub<T>> T.withAuthToken(
    token: String
): T = addOption("authorization", token).also {
    Timber.d { "Auth token: $token" }
}

fun <T : AbstractCoroutineStub<T>> T.withClientOrigin(
    origin: String = "Android"
): T = addMetaData("ClientOrigin", origin)

fun <T : AbstractCoroutineStub<T>> T.withTotpCode(
    totp: String
): T = addOption("FactorTotp", totp)
