package org.example.partner;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/partners")
public class PartnerController {

    private final PartnerStorageService storage;
    private final PartnerAdminService adminService;

    public PartnerController(PartnerStorageService storage, PartnerAdminService adminService) {
        this.storage = storage;
        this.adminService = adminService;
    }

    @CrossOrigin(origins = {"http://localhost:8080", "http://127.0.0.1:8080"})
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> uploadPartnerRdf(
            @RequestParam("partnerName") String partnerName,
            @RequestParam("rdf") MultipartFile rdf
    ) throws IOException {
        Map<String, Object> res = new HashMap<>();
        if (partnerName == null || partnerName.isBlank()) {
            res.put("error", "partnerName is required");
            return ResponseEntity.badRequest().body(res);
        }
        if (rdf == null || rdf.isEmpty()) {
            res.put("error", "rdf file is required");
            return ResponseEntity.badRequest().body(res);
        }

        Path saved = storage.savePartnerFile(partnerName, "rdf", rdf);
        PartnerSubmission submission = adminService.createSubmission(partnerName.trim(), saved);

        res.put("id", submission.getId());
        res.put("status", submission.getStatus().name());
        return ResponseEntity.ok(res);
    }
}
