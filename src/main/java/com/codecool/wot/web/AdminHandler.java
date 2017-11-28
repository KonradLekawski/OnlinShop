package com.codecool.wot.web;

import com.codecool.wot.dao.*;
import com.codecool.wot.model.Admin;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;


public class AdminHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String cookieStr = httpExchange.getRequestHeaders().getFirst("Cookie");

        if (cookieStr != null) {
            URI uri = httpExchange.getRequestURI();
            Integer userId = CookieDAO.getInstance().getCookie(cookieStr).getUserId();

            Admin admin = null;
            if (PersonDAO.getInstance().getPerson(userId) instanceof Admin) {
                admin = (Admin)PersonDAO.getInstance().getPerson(userId);
            }


            if (admin != null && Integer.toString(userId).equals(parseURIToGetId(uri.getPath()))) {
                JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/admin.html");
                JtwigModel model = JtwigModel.newModel();

                model.with("name", admin.getName());
                model.with("classes", ClassDAO.getInstance().read());
                String response = template.render(model);

                httpExchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = httpExchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else {
                handleWrongUser(httpExchange);
            }
        } else {
            handleWrongUser(httpExchange);
        }
    }


    private void handleWrongUser(HttpExchange httpExchange) throws IOException {
        httpExchange.getResponseHeaders().set("Location", "/");
        httpExchange.sendResponseHeaders(302,-1);
    }

    private String parseURIToGetId(String uri) {
        String userIdFromURI = "";
        String[] pairs = uri.split("/");
        try {
            userIdFromURI = pairs[2];
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        return userIdFromURI;
    }


}
