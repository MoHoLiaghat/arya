package com.github.kotgi.grpc

import io.grpc.Channel
import io.grpc.StatusRuntimeException
import com.github.kotgi.greeting.GreeterGrpc
import com.github.kotgi.greeting.GreeterGrpc.GreeterBlockingStub
import com.github.kotgi.greeting.Greeting.ClientInput
import com.github.kotgi.greeting.Greeting.ServerOutput
import java.util.logging.Level
import java.util.logging.Logger

class GreetClient(channel: Channel) {
    private val blockingStub: GreeterBlockingStub

    init {
        blockingStub = GreeterGrpc.newBlockingStub(channel)
    }

    fun makeGreeting(greeting: String, username: String) {
        logger.info("Sending greeting to server: $greeting for name: $username")
        val request = ClientInput.newBuilder().setName(username).setGreeting(greeting).build()
        logger.info("Sending to server: $request")
        val response: ServerOutput
        response = try {
            blockingStub.greet(request)
        } catch (e: StatusRuntimeException) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.status)
            return
        }
        logger.info("Got following from the server: " + response.message)
    }

    companion object {
        private val logger: Logger = Logger.getLogger(GreetClient::class.java.name)

/*
        @Throws(Exception::class)
        @JvmStatic
        fun main(args: Array<String>) {
            val greeting = "args[0]"
            val username = "args[1]"
            val serverAddress = "localhost:50051"
            val channel = ManagedChannelBuilder.forTarget(serverAddress)
                .usePlaintext()
                .build()
            try {
                val client = GreetClient(channel)
                client.makeGreeting(greeting, username)
            } finally {
                channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS)
            }
        }
*/
    }
}