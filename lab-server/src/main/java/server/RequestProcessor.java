package server;

import controller.CommandHandler;
import models.Ticket;
import ui.Request;
import ui.Response;
import ui.Serialization;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class RequestProcessor extends Thread {
    private static final Logger logger = LogManager.getLogger(RequestProcessor.class);

    private final DatagramChannel channel;
    private final InetSocketAddress clientAddress;
    private final byte[] requestData;
    private final CommandHandler commandHandler;

    public RequestProcessor(DatagramChannel channel, InetSocketAddress clientAddress,
                            byte[] requestData, CommandHandler commandHandler) {
        this.channel = channel;
        this.clientAddress = clientAddress;
        this.requestData = requestData;
        this.commandHandler = commandHandler;
    }

    @Override
    public void run() {
        try {
            Request request = Serialization.deserialize(requestData);
            logger.debug("Received request from {}: {}", clientAddress, request);

            if ("ping".equals(request.getCommandName())) {
                Response pingResponse = Response.success("pong");
                sendResponse(pingResponse);
                return;
            }

            Response response = commandHandler.handle(request);
            sendResponse(response);
        } catch (Exception e) {
            logger.error("Error processing request: {}", e.getMessage());
            sendResponse(Response.error("Internal server error"));
        }
    }

    private void sendResponse(Response response) {
        try {
            byte[] responseData = Serialization.serialize(response);
            ByteBuffer buffer = ByteBuffer.wrap(responseData);
            channel.send(buffer, clientAddress);
            logger.debug("Sent response to {}", clientAddress);
        } catch (IOException e) {
            logger.error("Failed to send response: {}", e.getMessage());
        }
    }
}