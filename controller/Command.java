package controller;

import service.CollectionManager;
import ui.Request;
import ui.Response;

public interface Command {
    Response execute(Request request, CollectionManager collectionManager);
}
