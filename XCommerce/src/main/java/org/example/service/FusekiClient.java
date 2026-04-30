package org.example.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;

@Service
public class FusekiClient {
    private final String[] queryUrls;
    private final String dataUrl;

    public FusekiClient(
            @Value("${fuseki.query-url:http://localhost:3030/Products/query}") String primaryQueryUrl,
            @Value("${fuseki.data-url:http://localhost:3030/Products/data}") String dataUrl
    ) {
        this.queryUrls = new String[] {
                primaryQueryUrl,
                "http://localhost:3030/Products/sparql",
                "http://localhost:3030/products/query",
                "http://localhost:3030/products/sparql"
        };
        this.dataUrl = dataUrl;
    }

    public String runSelectQuery(String sparql) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(java.util.List.of(MediaType.APPLICATION_JSON));

        String encodedQuery = URLEncoder.encode(sparql, StandardCharsets.UTF_8);
        String body = "query=" + encodedQuery;
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        HttpHeaders getHeaders = new HttpHeaders();
        getHeaders.setAccept(java.util.List.of(MediaType.APPLICATION_JSON));
        HttpEntity<Void> getRequest = new HttpEntity<>(getHeaders);

        HttpClientErrorException lastClientError = null;

        for (String url : queryUrls) {
            try {
                ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
                return response.getBody();
            } catch (HttpClientErrorException e) {
                lastClientError = e;
                if (!(e instanceof HttpClientErrorException.MethodNotAllowed) &&
                        !(e instanceof HttpClientErrorException.NotFound)) {
                    throw e;
                }
            }

            try {
                URI uri = UriComponentsBuilder
                        .fromHttpUrl(url)
                        .queryParam("query", encodedQuery)
                        .build(true)
                        .toUri();

                ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, getRequest, String.class);
                return response.getBody();
            } catch (HttpClientErrorException e) {
                lastClientError = e;
                // try next URL
            }
        }

        if (lastClientError != null) {
            throw lastClientError;
        }
        throw new IllegalStateException("Fuseki query failed for unknown reasons");
    }

    public void uploadRdfFile(Path rdfFile, String contentType, String graphUri) throws IOException {
        byte[] body = Files.readAllBytes(rdfFile);
        uploadBytes(body, contentType, graphUri);
    }

    public void uploadBytes(byte[] body, String contentType, String graphUri) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(contentType));
        headers.setBasicAuth("admin", "admin123");
        HttpEntity<byte[]> request = new HttpEntity<>(body, headers);
        String targetUrl = graphUri == null ? dataUrl : dataUrl + "?graph=" + graphUri;
        restTemplate.exchange(targetUrl, HttpMethod.POST, request, String.class);
    }

    public void deleteGraph(String graphUri) {
        if (graphUri == null) return;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("admin", "admin123");
        HttpEntity<Void> request = new HttpEntity<>(headers);
        String targetUrl = dataUrl + "?graph=" + graphUri;
        restTemplate.exchange(targetUrl, HttpMethod.DELETE, request, String.class);
    }
}