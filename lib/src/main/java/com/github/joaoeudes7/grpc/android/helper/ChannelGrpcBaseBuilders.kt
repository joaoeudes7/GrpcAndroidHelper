@file:Suppress("unused")

package com.github.joaoeudes7.grpc.android.helper

import android.content.*
import com.github.joaoeudes7.grpc.android.helper.logging.ClientGrpcLogInterceptor
import io.grpc.android.*
import io.grpc.okhttp.*
import kotlinx.coroutines.*
import java.util.concurrent.*
import javax.net.ssl.*

object ChannelGrpcBaseBuilders {
    private val intercept = ClientGrpcLogInterceptor()

    fun createDefaultChannelConfig(
        target: String,
        coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
    ): OkHttpChannelBuilder = OkHttpChannelBuilder
        .forTarget(target)
        .executor(coroutineDispatcher.asExecutor())
        .enableRetry()
        .maxRetryAttempts(4)
        .hostnameVerifier { _, _ -> true }
        .keepAliveTime(1, TimeUnit.MINUTES)
        .keepAliveTimeout(1, TimeUnit.MINUTES)
        .maxInboundMessageSize(1024 * 1024 * 10)
        .intercept(intercept)

    fun createChannelSecure(
        target: String,
        sslSocketFactory: SSLSocketFactory,
        coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
    ): OkHttpChannelBuilder = createDefaultChannelConfig(
        target,
        coroutineDispatcher
    )
        .useTransportSecurity()
        .sslSocketFactory(sslSocketFactory)


    fun createChannelSecure(
        target: String,
        coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
    ): OkHttpChannelBuilder =
        createDefaultChannelConfig(
            target,
            coroutineDispatcher
        ).useTransportSecurity()

    fun createDefaultChannelPlainText(
        context: Context,
        target: String,
        coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
    ): AndroidChannelBuilder = AndroidChannelBuilder.usingBuilder(
        createDefaultChannelConfig(target, coroutineDispatcher)
    ).context(context)

}
