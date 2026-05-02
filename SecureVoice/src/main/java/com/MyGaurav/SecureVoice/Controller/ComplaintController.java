package com.MyGaurav.SecureVoice.Controller;

import com.MyGaurav.SecureVoice.Entity.Complaints;
import com.MyGaurav.SecureVoice.Service.ComplaintService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

// @RestController = this class handles HTTP requests and returns JSON
// @RequestMapping = all URLs in this class start with /api
@RestController
@RequestMapping("/api")
public class ComplaintController {

    @Autowired
    private ComplaintService service;

    // ────────────────────────────────────────────────────────────
    // POST /api/complaint
    // User submits a new complaint
    // ────────────────────────────────────────────────────────────
    @PostMapping("/complaint")
    public ResponseEntity<Map<String, Object>> submitComplaint(
            @RequestBody Map<String, String> body,  // reads JSON from frontend
            HttpServletRequest request) {

        String message  = body.get("message");
        String category = body.get("category");

        try {
            String trackingId = service.submitComplaint(message, category);

            // Build the success response
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("trackingId", trackingId);
            response.put("message", "Complaint submitted! Save your tracking ID.");

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            // Something went wrong (validation error etc)
            return ResponseEntity.badRequest().body(error(e.getMessage()));
        }
    }

    // ────────────────────────────────────────────────────────────
    // GET /api/complaint/{trackingId}
    // User checks the status of their complaint
    // ────────────────────────────────────────────────────────────
    @GetMapping("/complaint/{trackingId}")
    public ResponseEntity<Map<String, Object>> trackComplaint(
            @PathVariable String trackingId) {  // {trackingId} from the URL

        Optional<Complaints> result = service.getByTrackingId(trackingId);

        if (result.isEmpty()) {
            // 404 = not found
            return ResponseEntity.status(404).body(error("No complaint found with that tracking ID."));
        }

        Complaints c = result.get();

        // Build response - only show what the user needs (NOT the message body, for privacy)
        Map<String, Object> response = new HashMap<>();
        response.put("trackingId", c.getTrackingId());
        response.put("status", c.getStatus().name());        // "SUBMITTED", "IN_REVIEW", "RESOLVED"
        response.put("category", c.getCategory());
        response.put("adminNotes", c.getAdminNotes());       // may be null
        response.put("submittedAt", c.getCreatedAt());

        return ResponseEntity.ok(response);
    }

    // ────────────────────────────────────────────────────────────
    // Helper method: build an error response map
    // ────────────────────────────────────────────────────────────
    private Map<String, Object> error(String message) {
        Map<String, Object> map = new HashMap<>();
        map.put("success", false);
        map.put("message", message);
        return map;
    }
}