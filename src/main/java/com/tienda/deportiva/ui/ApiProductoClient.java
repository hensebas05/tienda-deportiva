package com.tienda.deportiva.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tienda.deportiva.model.Producto;
import org.apache.hc.client5.http.classic.methods.*;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.util.Arrays;
import java.util.List;

public class ApiProductoClient {
    private static final String BASE_URL = "http://localhost:8080/api/productos";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CloseableHttpClient httpClient = HttpClients.createDefault();

    public List<Producto> obtenerTodos() throws Exception {
        HttpGet request = new HttpGet(BASE_URL);
        return httpClient.execute(request, response -> {
            String body = new String(response.getEntity().getContent().readAllBytes());
            return Arrays.asList(objectMapper.readValue(body, Producto[].class));
        });
    }

    public Producto obtenerPorId(Long id) throws Exception {
        HttpGet request = new HttpGet(BASE_URL + "/" + id);
        return httpClient.execute(request, response -> {
            String body = new String(response.getEntity().getContent().readAllBytes());
            return objectMapper.readValue(body, Producto.class);
        });
    }

    public List<Producto> buscar(String nombre) throws Exception {
        HttpGet request = new HttpGet(BASE_URL + "/buscar?nombre=" + nombre);
        return httpClient.execute(request, response -> {
            String body = new String(response.getEntity().getContent().readAllBytes());
            return Arrays.asList(objectMapper.readValue(body, Producto[].class));
        });
    }

    public Producto crear(Producto producto) throws Exception {
        HttpPost request = new HttpPost(BASE_URL);
        String json = objectMapper.writeValueAsString(producto);
        request.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
        request.setHeader("Content-Type", "application/json");

        return httpClient.execute(request, response -> {
            String body = new String(response.getEntity().getContent().readAllBytes());
            if (response.getCode() >= 400) {
                throw new Exception("Error al crear: " + body);
            }
            return objectMapper.readValue(body, Producto.class);
        });
    }

    public Producto actualizar(Long id, Producto producto) throws Exception {
        HttpPut request = new HttpPut(BASE_URL + "/" + id);
        String json = objectMapper.writeValueAsString(producto);
        request.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
        request.setHeader("Content-Type", "application/json");

        return httpClient.execute(request, response -> {
            String body = new String(response.getEntity().getContent().readAllBytes());
            if (response.getCode() >= 400) {
                throw new Exception("Error al actualizar: " + body);
            }
            return objectMapper.readValue(body, Producto.class);
        });
    }

    public void eliminar(Long id) throws Exception {
        HttpDelete request = new HttpDelete(BASE_URL + "/" + id);
        httpClient.execute(request, response -> {
            if (response.getCode() >= 400) {
                String body = new String(response.getEntity().getContent().readAllBytes());
                throw new Exception("Error al eliminar: " + body);
            }
            return null;
        });
    }
}
