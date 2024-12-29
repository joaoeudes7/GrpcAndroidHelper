@file:Suppress("unused")

package com.github.joaoeudes7.grpc.android.helper.extensions

import com.github.ajalt.timberkt.*
import io.grpc.*
import io.grpc.kotlin.AbstractCoroutineStub
import io.grpc.stub.MetadataUtils

fun Metadata.put(key: String, value: String) {
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
    token: String,
    headerKey: String = "Authorization"
): T = addOption(headerKey, token).also {
    Timber.d { "Auth token: $token" }
}

fun <T : AbstractCoroutineStub<T>> T.withClientOrigin(
    origin: String = "Android",
    headerKey: String = "ClientOrigin"
): T = addMetaData(headerKey, origin)

fun <T : AbstractCoroutineStub<T>> T.withTotpCode(
    totp: String,
    headerKey: String = "FactorTotp"
): T = addOption(headerKey, totp)
