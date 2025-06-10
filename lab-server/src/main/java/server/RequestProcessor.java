package server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.UserService;
import ui.CommandHandler;
import ui.Request;
import ui.Response;
import ui.Serialization;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.ForkJoinPool;

public class RequestProcessor {
    private static final Logger logger = LogManager.getLogger(RequestProcessor.class);

    private final DatagramChannel channel;
    private final InetSocketAddress clientAddress;
    private final byte[] requestData;
    private final CommandHandler commandHandler;
    private final UserService userService;
    private final ForkJoinPool responsePool;

    public RequestProcessor(DatagramChannel channel, InetSocketAddress clientAddress,
                          byte[] requestData, CommandHandler commandHandler, UserService userService,
                          ForkJoinPool responsePool) {
        this.channel = channel;
        this.clientAddress = clientAddress;
        this.requestData = requestData;
        this.commandHandler = commandHandler;
        this.userService = userService;
        this.responsePool = responsePool;
    }

    public void process() {
        try {
            Request request = Serialization.deserialize(requestData);
            logger.debug("Received request from {}: {}", clientAddress, request);

            if ("ping".equals(request.getCommandName())) {
                Response pingResponse = Response.success("pong");
                sendResponse(pingResponse);
                return;
            }

            Response response = commandHandler.handle(request, userService.getUserByToken(request.getUserToken()));
            sendResponse(response);
        } catch (Exception e) {
            logger.error("Error processing request: {}", e.getMessage());
            sendResponse(Response.error("Internal server error"));
        }
    }

    private void sendResponse(Response response) {
        responsePool.submit(() -> {
            try {
                byte[] responseData = Serialization.serialize(response);
                ByteBuffer buffer = ByteBuffer.wrap(responseData);
                channel.send(buffer, clientAddress);
                logger.debug("Sent response to {}", clientAddress);
            } catch (IOException e) {
                logger.error("Failed to send response: {}", e.getMessage());
            }
        });
    }
}