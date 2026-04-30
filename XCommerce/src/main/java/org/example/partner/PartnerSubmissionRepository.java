package org.example.partner;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PartnerSubmissionRepository extends JpaRepository<PartnerSubmission, Long> {
    List<PartnerSubmission> findAllByStatusOrderByCreatedAtDesc(PartnerStatus status);
}
