package org.example.partner;

import org.example.service.FusekiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;

@Service
public class PartnerAdminService {

    private final PartnerSubmissionRepository repo;
    private final FusekiClient fusekiClient;

    public PartnerAdminService(PartnerSubmissionRepository repo, FusekiClient fusekiClient) {
        this.repo = repo;
        this.fusekiClient = fusekiClient;
    }

    public PartnerSubmission createSubmission(String partnerName, Path rdfPath) {
        PartnerSubmission s = new PartnerSubmission(partnerName, PartnerStatus.PENDING, rdfPath.toString(), Instant.now());
        return repo.save(s);
    }

    public List<PartnerSubmission> listAll() {
        return repo.findAll();
    }

    public List<PartnerSubmission> listPending() {
        return repo.findAllByStatusOrderByCreatedAtDesc(PartnerStatus.PENDING);
    }

    public PartnerSubmission approve(long id) {
        PartnerSubmission s = repo.findById(id).orElseThrow();
        s.setStatus(PartnerStatus.APPROVED);
        s.setApprovedAt(Instant.now());
        return repo.save(s);
    }

    public PartnerSubmission reject(long id) {
        PartnerSubmission s = repo.findById(id).orElseThrow();
        s.setStatus(PartnerStatus.REJECTED);
        return repo.save(s);
    }

    public void loadPartnerRdfToFuseki(long id, String contentType) throws IOException {
        PartnerSubmission s = repo.findById(id).orElseThrow();
        if (s.getStatus() != PartnerStatus.APPROVED) {
            throw new IllegalStateException("Partner not approved");
        }
        fusekiClient.uploadRdfFile(Path.of(s.getRdfFilePath()), contentType, "urn:partner:data:" + id);
    }

    public void delete(long id) {
        PartnerSubmission s = repo.findById(id).orElseThrow();
        s.setStatus(PartnerStatus.DELETED);
        repo.save(s);
        fusekiClient.deleteGraph("urn:partner:data:" + id);
        fusekiClient.deleteGraph("urn:partner:mapping:" + id);
    }

    public PartnerSubmission attachMapping(long id, Path mappingPath) {
        PartnerSubmission s = repo.findById(id).orElseThrow();
        s.setMappingFilePath(mappingPath.toString());
        return repo.save(s);
    }

    public PartnerSubmission attachMapping(long id, Path mappingPath, String originalFileName) {
        PartnerSubmission s = repo.findById(id).orElseThrow();
        s.setMappingFilePath(mappingPath.toString());
        s.setMappingOriginalFileName(originalFileName);
        return repo.save(s);
    }

    public void loadMappingToFuseki(long id, String contentType) throws IOException {
        PartnerSubmission s = repo.findById(id).orElseThrow();
        if (s.getMappingFilePath() == null || s.getMappingFilePath().isBlank()) {
            throw new IllegalStateException("No mapping file uploaded");
        }
        fusekiClient.uploadRdfFile(Path.of(s.getMappingFilePath()), contentType, "urn:partner:mapping:" + id);
    }

    public void deleteDataFromFuseki(long id) {
        fusekiClient.deleteGraph("urn:partner:data:" + id);
    }

    public void deleteMappingFromFuseki(long id) {
        fusekiClient.deleteGraph("urn:partner:mapping:" + id);
    }

    public List<String> listAllGraphs() {
        String sparql = "SELECT DISTINCT ?g WHERE { GRAPH ?g { ?s ?p ?o } }";
        try {
            String rawJson = fusekiClient.runSelectQuery(sparql);
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            com.fasterxml.jackson.databind.JsonNode root = mapper.readTree(rawJson);
            com.fasterxml.jackson.databind.JsonNode bindings = root.path("results").path("bindings");
            List<String> graphs = new java.util.ArrayList<>();
            if (bindings.isArray()) {
                for (com.fasterxml.jackson.databind.JsonNode b : bindings) {
                    com.fasterxml.jackson.databind.JsonNode gNode = b.path("g").path("value");
                    if (gNode.isTextual()) {
                        graphs.add(gNode.asText());
                    }
                }
            }
            return graphs;
        } catch (Exception e) {
            return java.util.Collections.emptyList();
        }
    }

    public void deleteAnyGraph(String graphUri) {
        fusekiClient.deleteGraph(graphUri);
    }
}
