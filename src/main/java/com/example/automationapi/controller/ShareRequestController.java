package com.example.automationapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.automationapi.model.ShareRequest;
import com.example.automationapi.repository.LocationUserRepository;
import com.example.automationapi.repository.ShareRequestRepository;
import com.example.automationapi.service.NotificationService;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/requests")
public class ShareRequestController {

    private final ShareRequestRepository repo;
    private final NotificationService notifier;
    private final LocationUserRepository userRepo;

    public ShareRequestController(
            ShareRequestRepository repo,
            NotificationService notifier,
            LocationUserRepository userRepo) {
        this.repo = repo;
        this.notifier = notifier;
        this.userRepo = userRepo;
    }

    // CREATE REQUEST
    @PostMapping
    public ResponseEntity<ShareRequest> createRequest(
            @RequestHeader("X-User-Mobile") String requesterMobile,
            @RequestBody RequestDto body) {

        if (requesterMobile == null || requesterMobile.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        String target = body.getTargetMobile();

        // validate both users exist
        if (!userRepo.existsById(requesterMobile) || !userRepo.existsById(target)) {
            return ResponseEntity.status(422).build();
        }

        ShareRequest r = new ShareRequest(requesterMobile, target, "PENDING");
        ShareRequest saved = repo.save(r);

        notifier.notifyUser(
                r.getTargetMobile(),
                "/queue/notifications",
                saved
        );

        return ResponseEntity.status(201).body(saved);
    }

    // TARGET USER GETS PENDING REQUESTS
    @GetMapping("/incoming")
    public List<ShareRequest> incoming(
            @RequestHeader("X-User-Mobile") String targetMobile) {

        return repo.findByTargetMobileAndStatus(targetMobile, "PENDING");
    }
    
    @GetMapping("/sent")
    public List<ShareRequest> sent(
            @RequestHeader("X-User-Mobile") String requesterMobile) {

        return repo.findByRequesterMobileAndStatusIn(
                requesterMobile,
                List.of("PENDING", "APPROVED")
        );
    }



    // TARGET APPROVES
    @PostMapping("/{id}/approve")
    public ResponseEntity<ShareRequest> approve(
            @RequestHeader("X-User-Mobile") String caller,
            @PathVariable Long id) {

        Optional<ShareRequest> opt = repo.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();

        ShareRequest r = opt.get();
        if (!r.getTargetMobile().equals(caller))
            return ResponseEntity.status(403).build();

        r.setStatus("APPROVED");
        r.setRespondedAt(Instant.now());

        ShareRequest saved = repo.save(r);
        notifier.notifyUser(
                r.getRequesterMobile(),
                "/queue/notifications",
                saved
        );

        return ResponseEntity.ok(saved);
    }

    // TARGET DECLINES
    @PostMapping("/{id}/decline")
    public ResponseEntity<ShareRequest> decline(
            @RequestHeader("X-User-Mobile") String caller,
            @PathVariable Long id) {

        Optional<ShareRequest> opt = repo.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();

        ShareRequest r = opt.get();
        if (!r.getTargetMobile().equals(caller))
            return ResponseEntity.status(403).build();

        r.setStatus("DECLINED");
        r.setRespondedAt(Instant.now());

        ShareRequest saved = repo.save(r);
        notifier.notifyUser(
                r.getRequesterMobile(),
                "/queue/notifications",
                saved
        );

        return ResponseEntity.ok(saved);
    }

    // REQUESTER REVOKES
    @PostMapping("/{id}/revoke")
    public ResponseEntity<ShareRequest> revoke(
            @RequestHeader("X-User-Mobile") String caller,
            @PathVariable Long id) {

        Optional<ShareRequest> opt = repo.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();

        ShareRequest r = opt.get();
        if (!r.getRequesterMobile().equals(caller))
            return ResponseEntity.status(403).build();

        r.setStatus("REVOKED");
        r.setRespondedAt(Instant.now());

        ShareRequest saved = repo.save(r);
        notifier.notifyUser(
                r.getTargetMobile(),
                "/queue/notifications",
                saved
        );

        return ResponseEntity.ok(saved);
    }

    // DTO
    public static class RequestDto {
        private String targetMobile;
        public String getTargetMobile() { return targetMobile; }
        public void setTargetMobile(String targetMobile) { this.targetMobile = targetMobile; }
    }
}
