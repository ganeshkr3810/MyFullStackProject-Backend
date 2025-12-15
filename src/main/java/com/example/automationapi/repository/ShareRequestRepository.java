package com.example.automationapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.automationapi.model.ShareRequest;
import java.util.List;

public interface ShareRequestRepository extends JpaRepository<ShareRequest, Long> {

    // Used by ShareRequestController -> incoming()
    List<ShareRequest> findByTargetMobileAndStatus(
            String targetMobile,
            String status
    );

    // Used by LocationController -> push approved users
    List<ShareRequest> findByRequesterMobileAndStatus(
            String requesterMobile,
            String status
    );

    // Used by LocationController -> GET authorization
    boolean existsByRequesterMobileAndTargetMobileAndStatus(
            String requesterMobile,
            String targetMobile,
            String status
    );
    
    List<ShareRequest> findByRequesterMobileAndStatusIn(
            String requesterMobile,
            List<String> statuses
    );

}
