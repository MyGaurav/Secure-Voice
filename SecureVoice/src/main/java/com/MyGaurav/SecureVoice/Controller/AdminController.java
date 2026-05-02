package com.MyGaurav.SecureVoice.Controller;

import com.MyGaurav.SecureVoice.Entity.Complaints;
import com.MyGaurav.SecureVoice.Service.ComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// This controller handles admin actions
// Spring Security automatically protects ALL /admin/** routes
// (configured in SecurityConfig.java)
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ComplaintService service;

    // ────────────────────────────────────────────────────────────
    // GET /admin/complaints
    // Returns all complaints to show in the admin dashboard table
    // ────────────────────────────────────────────────────────────
    @GetMapping("/complaints")
    public ResponseEntity<List<Complaints>> getAllComplaints() {
        List<Complaints> complaints = service.getAllComplaints();
        return ResponseEntity.ok(complaints);
    }

    // ────────────────────────────────────────────────────────────
    // PUT /admin/update
    // Admin changes the status and optionally adds a note
    // ────────────────────────────────────────────────────────────
    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> updateStatus(
            @RequestBody Map<String, String> body) {

        try {
            Long id = Long.parseLong(body.get("id"));
            String newStatus  = body.get("status");     // "IN_REVIEW" or "RESOLVED"
            String adminNotes = body.get("adminNotes"); // optional

            Complaints updated = service.updateStatus(id, newStatus, adminNotes);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Status updated to " + updated.getStatus());
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            Map<String, Object> err = new HashMap<>();
            err.put("success", false);
            err.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(err);
        }
    }

    // ────────────────────────────────────────────────────────────
    // GET /admin/stats
    // Quick count summary for dashboard tiles
    // ────────────────────────────────────────────────────────────
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getStats() {
        List<Complaints> all = service.getAllComplaints();

        // Count each status using Java streams (filter + count)
        long submitted = all.stream().filter(c -> c.getStatus() == Complaints.Status.SUBMITTED).count();
        long inReview  = all.stream().filter(c -> c.getStatus() == Complaints.Status.IN_REVIEW).count();
        long resolved  = all.stream().filter(c -> c.getStatus() == Complaints.Status.RESOLVED).count();

        Map<String, Long> stats = new HashMap<>();
        stats.put("total",     (long) all.size());
        stats.put("submitted", submitted);
        stats.put("inReview",  inReview);
        stats.put("resolved",  resolved);

        return ResponseEntity.ok(stats);
    }
}