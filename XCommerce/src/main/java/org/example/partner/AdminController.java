package org.example.partner;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final String adminKey;
    private final PartnerAdminService adminService;
    private final PartnerStorageService storage;

    public AdminController(
            @Value("${xcommerce.admin.key:change-me}") String adminKey,
            PartnerAdminService adminService,
            PartnerStorageService storage
    ) {
        this.adminKey = adminKey;
        this.adminService = adminService;
        this.storage = storage;
    }

    private void requireAdminKey(String key) {
        if (key == null || !key.equals(adminKey)) {
            throw new IllegalArgumentException("Invalid admin key");
        }
    }

    @CrossOrigin(origins = {"http://localhost:8080", "http://127.0.0.1:8080"})
    @GetMapping(value = "/partners", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> listPartners(@RequestParam("key") String key) {
        requireAdminKey(key);
        List<PartnerSubmissionDto> all = adminService.listAll().stream().map(PartnerSubmissionDto::from).collect(Collectors.toList());
        Map<String, Object> res = new HashMap<>();
        res.put("partners", all);
        return ResponseEntity.ok(res);
    }

    @CrossOrigin(origins = {"http://localhost:8080", "http://127.0.0.1:8080"})
    @PostMapping(value = "/partners/{id}/approve", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> approve(@PathVariable("id") long id, @RequestParam("key") String key) {
        requireAdminKey(key);
        PartnerSubmission s = adminService.approve(id);
        Map<String, Object> res = new HashMap<>();
        res.put("id", s.getId());
        res.put("status", s.getStatus().name());
        return ResponseEntity.ok(res);
    }

    @CrossOrigin(origins = {"http://localhost:8080", "http://127.0.0.1:8080"})
    @PostMapping(value = "/partners/{id}/reject", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> reject(@PathVariable("id") long id, @RequestParam("key") String key) {
        requireAdminKey(key);
        PartnerSubmission s = adminService.reject(id);
        Map<String, Object> res = new HashMap<>();
        res.put("id", s.getId());
        res.put("status", s.getStatus().name());
        return ResponseEntity.ok(res);
    }

    @CrossOrigin(origins = {"http://localhost:8080", "http://127.0.0.1:8080"})
    @PostMapping(value = "/partners/{id}/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> delete(@PathVariable("id") long id, @RequestParam("key") String key) {
        requireAdminKey(key);
        adminService.delete(id);
        Map<String, Object> res = new HashMap<>();
        res.put("deleted", true);
        return ResponseEntity.ok(res);
    }

    @CrossOrigin(origins = {"http://localhost:8080", "http://127.0.0.1:8080"})
    @PostMapping(value = "/partners/{id}/load-rdf", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> loadRdf(
            @PathVariable("id") long id,
            @RequestParam("key") String key,
            @RequestParam(value = "contentType", defaultValue = "application/rdf+xml") String contentType
    ) throws IOException {
        requireAdminKey(key);
        adminService.loadPartnerRdfToFuseki(id, contentType);
        Map<String, Object> res = new HashMap<>();
        res.put("loaded", true);
        return ResponseEntity.ok(res);
    }

    @CrossOrigin(origins = {"http://localhost:8080", "http://127.0.0.1:8080"})
    @PostMapping(value = "/partners/{id}/upload-mapping", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> uploadMapping(
            @PathVariable("id") long id,
            @RequestParam("key") String key,
            @RequestParam("mapping") MultipartFile mapping
    ) throws IOException {
        requireAdminKey(key);
        if (mapping == null || mapping.isEmpty()) {
            Map<String, Object> res = new HashMap<>();
            res.put("error", "mapping file is required");
            return ResponseEntity.badRequest().body(res);
        }
        String originalName = mapping.getOriginalFilename();
        PartnerSubmissionDto dto = PartnerSubmissionDto.from(
                adminService.attachMapping(
                        id,
                        storage.savePartnerFile("partner_" + id, "mapping", mapping),
                        originalName
                )
        );
        Map<String, Object> res = new HashMap<>();
        res.put("partner", dto);
        return ResponseEntity.ok(res);
    }

    @CrossOrigin(origins = {"http://localhost:8080", "http://127.0.0.1:8080"})
    @PostMapping(value = "/partners/{id}/load-mapping", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> loadMapping(
            @PathVariable("id") long id,
            @RequestParam("key") String key,
            @RequestParam(value = "contentType", defaultValue = "application/rdf+xml") String contentType
    ) throws IOException {
        requireAdminKey(key);
        adminService.loadMappingToFuseki(id, contentType);
        Map<String, Object> res = new HashMap<>();
        res.put("loaded", true);
        return ResponseEntity.ok(res);
    }

    @CrossOrigin(origins = {"http://localhost:8080", "http://127.0.0.1:8080"})
    @PostMapping(value = "/partners/{id}/delete-data-graph", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> deleteDataGraph(@PathVariable("id") long id, @RequestParam("key") String key) {
        requireAdminKey(key);
        adminService.deleteDataFromFuseki(id);
        Map<String, Object> res = new HashMap<>();
        res.put("deleted", true);
        return ResponseEntity.ok(res);
    }

    @CrossOrigin(origins = {"http://localhost:8080", "http://127.0.0.1:8080"})
    @PostMapping(value = "/partners/{id}/delete-mapping-graph", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> deleteMappingGraph(@PathVariable("id") long id, @RequestParam("key") String key) {
        requireAdminKey(key);
        adminService.deleteMappingFromFuseki(id);
        Map<String, Object> res = new HashMap<>();
        res.put("deleted", true);
        return ResponseEntity.ok(res);
    }

    @CrossOrigin(origins = {"http://localhost:8080", "http://127.0.0.1:8080"})
    @GetMapping(value = "/graphs", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> listGraphs(@RequestParam("key") String key) {
        requireAdminKey(key);
        List<String> graphs = adminService.listAllGraphs();
        Map<String, Object> res = new HashMap<>();
        res.put("graphs", graphs);
        return ResponseEntity.ok(res);
    }

    @CrossOrigin(origins = {"http://localhost:8080", "http://127.0.0.1:8080"})
    @PostMapping(value = "/graphs/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> deleteAnyGraph(@RequestParam("key") String key, @RequestParam("graphUri") String graphUri) {
        requireAdminKey(key);
        adminService.deleteAnyGraph(graphUri);
        Map<String, Object> res = new HashMap<>();
        res.put("deleted", true);
        return ResponseEntity.ok(res);
    }
}
