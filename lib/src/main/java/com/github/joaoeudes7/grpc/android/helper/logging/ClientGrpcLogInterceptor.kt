package com.github.joaoeudes7.grpc.android.helper.logging

import io.grpc.*
import timber.log.Timber

class ClientGrpcLogInterceptor : ClientInterceptor {
  override fun <ReqT, RespT> interceptCall(
    method: MethodDescriptor<ReqT, RespT>?,
    callOptions: CallOptions?,
    next: Channel
  ) = object : ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(
    next.newCall(method, callOptions)
  ) {

    override fun sendMessage(message: ReqT) {
      Timber.i("⚡ GRPC Request: ${method!!.fullMethodName}}")

      super.sendMessage(message)
    }

    override fun cancel(message: String?, cause: Throwable?) {
      Timber.i("⚡ ERROR IN GRPC: ${method!!.fullMethodName}}")

      super.cancel(message, cause)
    }

    override fun start(responseListener: Listener<RespT>, headers: Metadata) {
      super.start(object : ForwardingClientCallListener.SimpleForwardingClientCallListener<RespT>(
        responseListener
      ) {
        override fun onMessage(message: RespT) {
          Timber.i("⚡ GRPC Response: ${method!!.fullMethodName}}")

          super.onMessage(message)
        }
      }, headers)
    }
  }
}
