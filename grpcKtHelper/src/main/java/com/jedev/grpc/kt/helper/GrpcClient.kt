package com.jedev.grpc.kt.helper

import io.grpc.*
import io.grpc.kotlin.*
import javax.net.ssl.SSLSocketFactory

sealed class GrpcClient<S : AbstractCoroutineStub<S>> {

    abstract val defaultManagedChannel: ManagedChannel
    abstract val stub: S

    open class Grpc<S : AbstractCoroutineStub<S>>(
        targetUrl: String,
        stubStart: (ManagedChannel) -> S
    ) : GrpcClient<S>() {

        final override val defaultManagedChannel: ManagedChannel by lazy {
            ChannelGrpcUtils.createDefaultChannelConfig(targetUrl).usePlaintext().build()
        }

        final override val stub: S by lazy {
            stubStart(defaultManagedChannel)
        }
    }

    open class GrpcSecure<S : AbstractCoroutineStub<S>>(
        targetUrl: String,
        stubStart: (ManagedChannel) -> S,
        sslCredentialsFiles: SSLSocketFactory
    ) : GrpcClient<S>() {
        final override val defaultManagedChannel: ManagedChannel by lazy {
            ChannelGrpcUtils.createChannelSecure(
                targetUrl,
                sslCredentialsFiles
            ).build()
        }

        final override val stub: S by lazy {
            stubStart(defaultManagedChannel)
        }
    }

    fun authenticate(token: String): S {
        return stub.withAuthToken(token)
    }
}
