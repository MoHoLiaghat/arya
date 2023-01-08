package com.github.kotgi.grpc

import io.grpc.Server
import io.grpc.ServerBuilder
import io.grpc.stub.StreamObserver
import com.github.kotgi.greeting.GreeterGrpc
import com.github.kotgi.greeting.Greeting.ClientInput
import com.github.kotgi.greeting.Greeting.ServerOutput
import java.io.IOException
import java.util.concurrent.TimeUnit
import java.util.logging.Logger

class GreetServer {
    private var server: Server? = null
    @Throws(IOException::class)
    private fun start() {
        val port = 50051
        server = ServerBuilder.forPort(port)
            .addService(GreeterImpl())
            .build()
            .start()

        logger.info("Server started, listening on $port")

        Runtime.getRuntime().addShutdownHook(object : Thread() {
            override fun run() {
                System.err.println("Shutting down gRPC server")
                try {
                    server!!.shutdown().awaitTermination(30, TimeUnit.SECONDS)
                } catch (e: InterruptedException) {
                    e.printStackTrace(System.err)
                }
            }
        })
    }

    internal class GreeterImpl : GreeterGrpc.GreeterImplBase() {
        override fun greet(req: ClientInput, responseObserver: StreamObserver<ServerOutput?>) {
            logger.info("Got request from client: $req")
            val reply: ServerOutput = ServerOutput.newBuilder().setMessage(
                "Server says " + "\"" + req.getGreeting() + " " + req.getName() + "\""
            ).build()
            responseObserver.onNext(reply)
            responseObserver.onCompleted()
        }
    }

    companion object {
        private val logger = Logger.getLogger(
            GreetServer::class.java.name
        )

/*
        @Throws(IOException::class, InterruptedException::class)
        @JvmStatic
        fun main(args: Array<String>) {
            val greetServer = GreetServer()
            greetServer.start()
            greetServer.server!!.awaitTermination()
        }
*/
    }
}