package com.MyGaurav.SecureVoice.Service;

import com.MyGaurav.SecureVoice.Entity.Complaints;

import com.MyGaurav.SecureVoice.Repository.ComplaintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// @Service = this class contains the business logic
// It sits between the Controller (which talks to user) and Repository (which talks to DB)
@Service
public class ComplaintService {

    // Spring automatically gives us an instance of ComplaintRepository
    // This is called "Dependency Injection" - a key viva term!
    @Autowired
    private ComplaintRepository repo;

    // ── 1. Submit a new complaint ──────────────────────────────
    public String submitComplaint(String message, String category) {

        // Validation: message must be at least 20 characters
        if (message == null || message.trim().length() < 20) {
            throw new RuntimeException("Complaint must be at least 20 characters long.");
        }

        // Validation: category must be one of our allowed values
        if (!category.equals("Academic") && !category.equals("Hostel")
                && !category.equals("Misconduct") && !category.equals("Other")) {
            throw new RuntimeException("Invalid category selected.");
        }

        // Create a new Complaint object and fill it in
        Complaints complaint = new Complaints();
        complaint.setMessage(message.trim());
        complaint.setCategory(category);
        complaint.setTrackingId(generateTrackingId());
        // Note: status and createdAt are set automatically by @PrePersist in the model

        // Save it to the database
        repo.save(complaint);

        // Return the tracking ID so we can show it to the user
        return complaint.getTrackingId();
    }

    // ── 2. Check status by tracking ID ────────────────────────
    public Optional<Complaints> getByTrackingId(String trackingId) {
        // Optional means: it might exist, or it might not
        // The controller will check if it's empty and show 404
        return repo.findByTrackingId(trackingId.toUpperCase());
    }

    // ── 3. Get all complaints (for admin) ─────────────────────
    public List<Complaints> getAllComplaints() {
        return repo.findAllByOrderByCreatedAtDesc();
    }

    // ── 4. Update status (admin only) ─────────────────────────
    public Complaints updateStatus(Long id, String newStatus, String adminNotes) {

        // Find the complaint by ID, throw error if not found
        Complaints complaint = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Complaint not found with ID: " + id));

        // Update the status
        complaint.setStatus(Complaints.Status.valueOf(newStatus)); // valueOf converts "IN_REVIEW" → Status.IN_REVIEW

        // Update notes if admin typed something
        if (adminNotes != null && !adminNotes.isBlank()) {
            complaint.setAdminNotes(adminNotes.trim());
        }

        // Save the changes back to database
        repo.save(complaint);

        return complaint;
    }

    // ── Helper: Generate a unique Tracking ID ─────────────────
    // Format: SVX-ABCD1234 (SVX prefix + 8 random characters)
    private String generateTrackingId() {
        String id;
        do {
            // UUID.randomUUID() gives us something like: "a1b2c3d4-e5f6-..."
            // We take first 8 characters and make uppercase
            String random = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
            id = "SVX-" + random;
        } while (repo.existsByTrackingId(id)); // keep trying if somehow duplicate (very rare)

        return id;
    }
}