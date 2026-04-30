package org.example.partner;

import java.nio.file.Paths;
import java.time.Instant;

public class PartnerSubmissionDto {
    private final Long id;
    private final String partnerName;
    private final PartnerStatus status;
    private final Instant createdAt;
    private final Instant approvedAt;
    private final boolean hasMapping;
    private final String mappingFileName;

    public PartnerSubmissionDto(Long id, String partnerName, PartnerStatus status, Instant createdAt, Instant approvedAt, boolean hasMapping, String mappingFileName) {
        this.id = id;
        this.partnerName = partnerName;
        this.status = status;
        this.createdAt = createdAt;
        this.approvedAt = approvedAt;
        this.hasMapping = hasMapping;
        this.mappingFileName = mappingFileName;
    }

    public static PartnerSubmissionDto from(PartnerSubmission s) {
        String mappingPath = s.getMappingFilePath();
        boolean hasMapping = mappingPath != null && !mappingPath.isBlank();
        String mappingFileName = null;
        if (hasMapping) {
            mappingFileName = s.getMappingOriginalFileName();
            if (mappingFileName != null && mappingFileName.isBlank()) {
                mappingFileName = null;
            }
        }
        return new PartnerSubmissionDto(
                s.getId(),
                s.getPartnerName(),
                s.getStatus(),
                s.getCreatedAt(),
                s.getApprovedAt(),
                hasMapping,
                mappingFileName
        );
    }

    public Long getId() {
        return id;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public PartnerStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getApprovedAt() {
        return approvedAt;
    }

    public boolean isHasMapping() {
        return hasMapping;
    }

    public String getMappingFileName() {
        return mappingFileName;
    }
}
