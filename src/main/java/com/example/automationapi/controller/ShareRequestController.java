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

    // ===============================
    // CREATE REQUEST (A → B)
    // ===============================
    @PostMapping
    public ResponseEntity<ShareRequest> createRequest(
            @RequestHeader("X-User-Mobile") String requesterMobile,
            @RequestBody RequestDto body) {

        System.out.println("📩 CREATE REQUEST CALLED");
        System.out.println("Requester header: [" + requesterMobile + "]");
        System.out.println("Target body: [" + body.getTargetMobile() + "]");

        if (requesterMobile == null || requesterMobile.isBlank()) {
            System.out.println("❌ Requester mobile missing");
            return ResponseEntity.badRequest().build();
        }

        String target = body.getTargetMobile();

        boolean requesterExists = userRepo.existsById(requesterMobile);
        boolean targetExists = userRepo.existsById(target);

        System.out.println("Requester exists: " + requesterExists);
        System.out.println("Target exists: " + targetExists);

        if (!requesterExists || !targetExists) {
            System.out.println("❌ Returning 422");
            return ResponseEntity.status(422).build();
        }

        ShareRequest r = new ShareRequest(requesterMobile, target, "PENDING");
        ShareRequest saved = repo.save(r);

        System.out.println("✅ Request saved, ID=" + saved.getId());
        System.out.println("🔔 Sending WS notification to TARGET = [" + target + "]");

        notifier.notifyUser(
                target,
                "/queue/notifications",
                saved
        );

        return ResponseEntity.status(201).body(saved);
    }

    // ===============================
    // TARGET USER (B) → INCOMING
    // ===============================
    @GetMapping("/incoming")
    public List<ShareRequest> incoming(
            @RequestHeader("X-User-Mobile") String targetMobile) {

        System.out.println("📥 INCOMING FETCH for [" + targetMobile + "]");
        return repo.findByTargetMobile(targetMobile);
    }

    // ===============================
    // REQUESTER USER (A) → SENT
    // ===============================
    @GetMapping("/sent")
    public List<ShareRequest> sent(
            @RequestHeader("X-User-Mobile") String requesterMobile) {

        return repo.findByRequesterMobileAndStatusIn(
                requesterMobile,
                List.of("PENDING", "APPROVED")
        );
    }

    // ===============================
    // TARGET APPROVES
    // ===============================
    @PostMapping("/{id}/approve")
    public ResponseEntity<ShareRequest> approve(
            @RequestHeader("X-User-Mobile") String caller,
            @PathVariable Long id) {

        System.out.println("✅ APPROVE called by [" + caller + "] for ID=" + id);

        Optional<ShareRequest> opt = repo.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();

        ShareRequest r = opt.get();
        if (!r.getTargetMobile().equals(caller))
            return ResponseEntity.status(403).build();

        r.setStatus("APPROVED");
        r.setRespondedAt(Instant.now());

        ShareRequest saved = repo.save(r);

        System.out.println("🔔 Sending WS notification to REQUESTER = [" +
                r.getRequesterMobile() + "]");

        notifier.notifyUser(
                r.getRequesterMobile(),
                "/queue/notifications",
                saved
        );

        return ResponseEntity.ok(saved);
    }

    // ===============================
    // DTO
    // ===============================
    public static class RequestDto {
        private String targetMobile;
        public String getTargetMobile() { return targetMobile; }
        public void setTargetMobile(String targetMobile) {
            this.targetMobile = targetMobile;
        }
    }
    
 // ===============================
 // REVOKE / STOP SHARING
 // Can be called by TARGET or REQUESTER
 // ===============================
 @PostMapping("/{id}/revoke")
 public ResponseEntity<Void> revoke(
         @RequestHeader("X-User-Mobile") String caller,
         @PathVariable Long id) {

     Optional<ShareRequest> opt = repo.findById(id);
     if (opt.isEmpty()) return ResponseEntity.notFound().build();

     ShareRequest r = opt.get();

     // Only requester OR target can revoke
     boolean allowed =
             r.getRequesterMobile().equals(caller) ||
             r.getTargetMobile().equals(caller);

     if (!allowed) {
         return ResponseEntity.status(403).build();
     }

     repo.delete(r);

     // 🔔 notify both sides
     notifier.notifyUser(
             r.getRequesterMobile(),
             "/queue/notifications",
             "REVOKED"
     );

     notifier.notifyUser(
             r.getTargetMobile(),
             "/queue/notifications",
             "REVOKED"
     );

     return ResponseEntity.ok().build();
 }

}
