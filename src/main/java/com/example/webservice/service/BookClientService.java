
package com.example.webservice.service;

import com.example.webservice.model.Book;
import javax.ws.rs.core.GenericType;
import org.glassfish.jersey.client.ClientConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.client.Entity;
import java.util.List;

@Service
public class BookClientService {

    // Injecting the management service URL from application.properties in the Web UI service
    @Value("${management.service.url}")
    private String managementServiceUrl;

    // Get all books
    public List<Book> getAllBooks() {
        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(managementServiceUrl + "/books");

        Response response = target.request(MediaType.APPLICATION_JSON).get();

        if (response.getStatus() != Response.Status.OK.getStatusCode()) {
            throw new RuntimeException("Failed to fetch books: " + response.getStatus());
        }

        return response.readEntity(new GenericType<List<Book>>() {});
    }

    // Get book by ID
    public Book getBookById(Long id) {
        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(managementServiceUrl + "/books/" + id);

        Response response = target.request(MediaType.APPLICATION_JSON).get();

        if (response.getStatus() != Response.Status.OK.getStatusCode()) {
            throw new RuntimeException("Failed to fetch book with ID " + id + ": " + response.getStatus());
        }

        return response.readEntity(Book.class);
    }

    // Method to add a book
    public void addBook(Book book) {
        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(managementServiceUrl + "/books");

        Response response = target.request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(book, MediaType.APPLICATION_JSON));

        if (response.getStatus() != Response.Status.CREATED.getStatusCode() && 
            response.getStatus() != Response.Status.OK.getStatusCode()) {
            throw new RuntimeException("Failed to add book: " + response.getStatus());
        }
    }

    // Method to update a book
    public void updateBook(Long id, Book book) {
        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(managementServiceUrl + "/books/" + id);

        Response response = target.request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(book, MediaType.APPLICATION_JSON));

        if (response.getStatus() != Response.Status.OK.getStatusCode()) {
            throw new RuntimeException("Failed to update book with ID " + id + ": " + response.getStatus());
        }
    }

    // Method to delete a book
    public void deleteBook(Long id) {
        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget target = client.target(managementServiceUrl + "/books/" + id);

        Response response = target.request().delete();

        if (response.getStatus() != Response.Status.NO_CONTENT.getStatusCode()) {
            throw new RuntimeException("Failed to delete book with ID " + id + ": " + response.getStatus());
        }
    }
}



