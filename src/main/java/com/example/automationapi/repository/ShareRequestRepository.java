package com.example.automationapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.automationapi.model.ShareRequest;

import java.util.List;

public interface ShareRequestRepository
        extends JpaRepository<ShareRequest, Long> {

    List<ShareRequest> findByTargetMobile(String targetMobile);

    List<ShareRequest> findByRequesterMobileAndStatusIn(
            String requesterMobile,
            List<String> status
    );

    List<ShareRequest> findByTargetMobileAndStatus(
            String targetMobile,
            String status
    );

    boolean existsByRequesterMobileAndTargetMobileAndStatus(
            String requesterMobile,
            String targetMobile,
            String status
    );
}

