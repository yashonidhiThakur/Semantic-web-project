package org.example.partner;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "partner_submissions")
public class PartnerSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String partnerName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PartnerStatus status;

    @Column(nullable = false)
    private String rdfFilePath;

    private String mappingFilePath;

    private String mappingOriginalFileName;

    @Column(nullable = false)
    private Instant createdAt;

    private Instant approvedAt;

    public PartnerSubmission() {
    }

    public PartnerSubmission(String partnerName, PartnerStatus status, String rdfFilePath, Instant createdAt) {
        this.partnerName = partnerName;
        this.status = status;
        this.rdfFilePath = rdfFilePath;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public PartnerStatus getStatus() {
        return status;
    }

    public void setStatus(PartnerStatus status) {
        this.status = status;
    }

    public String getRdfFilePath() {
        return rdfFilePath;
    }

    public void setRdfFilePath(String rdfFilePath) {
        this.rdfFilePath = rdfFilePath;
    }

    public String getMappingFilePath() {
        return mappingFilePath;
    }

    public void setMappingFilePath(String mappingFilePath) {
        this.mappingFilePath = mappingFilePath;
    }

    public String getMappingOriginalFileName() {
        return mappingOriginalFileName;
    }

    public void setMappingOriginalFileName(String mappingOriginalFileName) {
        this.mappingOriginalFileName = mappingOriginalFileName;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(Instant approvedAt) {
        this.approvedAt = approvedAt;
    }
}
