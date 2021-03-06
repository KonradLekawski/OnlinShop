package com.codecool.wot.web;

import com.codecool.wot.dao.*;
import com.codecool.wot.model.*;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpCookie;
import java.net.URLDecoder;
import java.util.*;

public class LoginHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String cookieStr = httpExchange.getRequestHeaders().getFirst("Cookie");

        if (cookieStr != null) {
            cookieHandle(cookieStr, httpExchange);
        } else {
            login(httpExchange);
        }
    }


    private void login(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();

        if (method.equals("POST")) {
            InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String formData = br.readLine();

            List<String> loginData = parseLoginFormData(formData);

            String login = loginData.get(0);
            String password = loginData.get(1);

            Account person = PersonDAO.getInstance().getPerson(login, password);

            if (person != null) {
                Integer personId = person.getId();
                String uri = "/";

                if (person instanceof Admin) {
                    cookie(personId, httpExchange);
                    uri = String.format("/admin/%s", personId);
                } else if (person instanceof Mentor) {
                    cookie(personId ,httpExchange);
                    uri = String.format("/mentor/%s", personId);
                } else if (person instanceof Student) {
                    cookie(personId, httpExchange);
                    uri = String.format("/student/%s", personId);
                }
                httpExchange.getResponseHeaders().set("Location", uri);
                httpExchange.sendResponseHeaders(302,-1);
            }
        }

        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/login-page.html");
        JtwigModel model = JtwigModel.newModel();
        String response = template.render(model);

        httpExchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private void cookie(Integer userId ,HttpExchange httpExchange) throws IOException {
        HttpCookie cookie = new HttpCookie("sessionId", UUID.randomUUID().toString());
        cookie.setMaxAge(-1);
        httpExchange.getResponseHeaders().add("Set-Cookie", cookie.toString());
        CookieDAO.getInstance().add(new Cookie(userId, cookie.toString()));
    }

    private List<String> parseLoginFormData(String formData) throws UnsupportedEncodingException {
        List<String> list = new ArrayList<>();
        String login;
        String password;
        String[] pairs = formData.split("&");

        try {
            login = new URLDecoder().decode(pairs[0].split("=")[1], "UTF-8");
            password = new URLDecoder().decode(pairs[1].split("=")[1], "UTF-8");
            list.add(login);
            list.add(password);
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return list;
    }

    private void cookieHandle(String cookieStr, HttpExchange httpExchange) throws IOException {
        Integer userId = CookieDAO.getInstance().getCookie(cookieStr).getUserId();

        Account person = PersonDAO.getInstance().getPerson(userId);

        if (person != null) {
            String uri = "";
            if (person instanceof Admin) {
                uri = String.format("/admin/%s", userId);
            } else if (person instanceof Mentor) {
                uri = String.format("/mentor/%s", userId);
            } else if (person instanceof Student) {
                uri = String.format("/student/%s", userId);
            }

            httpExchange.getResponseHeaders().set("Location", uri);
            httpExchange.sendResponseHeaders(302, -1);
        }
    }
}
