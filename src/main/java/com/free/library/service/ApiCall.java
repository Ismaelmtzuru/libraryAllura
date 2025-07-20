package com.free.library.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


@Service
public class ApiCall {

    private final HttpClient httpClient;

    public ApiCall() {
        this.httpClient = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS) // <- importante
                .build();
    }

    public String obtenerDatos(String url) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return response.body();
            } else {

                throw new RuntimeException("Error al llamar a la API. Código de estado: " + response.statusCode() + ", Cuerpo de respuesta: " + response.body());
            }
        } catch (IOException e) {

            throw new RuntimeException("Error de conexión al intentar obtener datos de la URL: " + url, e);
        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
            throw new RuntimeException("La operación fue interrumpida al obtener datos de la URL: " + url, e);
        }
    }
}