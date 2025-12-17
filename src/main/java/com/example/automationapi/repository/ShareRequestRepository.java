package com.example.automationapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.automationapi.model.ShareRequest;
import java.util.List;

public interface ShareRequestRepository extends JpaRepository<ShareRequest, Long> {

    // 🔴 Incoming (target user sees ALL)
    List<ShareRequest> findByTargetMobile(String targetMobile);

    // Sent requests
    List<ShareRequest> findByRequesterMobileAndStatusIn(
        String requesterMobile,
        List<String> status
    );

    // 🔴 Approved where TARGET is sender
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
