package com.MyGaurav.SecureVoice.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// @Entity means: "This Java class = one table in the database"
// Spring will automatically create a table called 'complaint' for us
@Entity
public class Complaints{

    // @Id = this is the primary key
    // @GeneratedValue = MySQL will auto-increment this number (1, 2, 3...)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The complaint text typed by the user
    // columnDefinition = "TEXT" because it can be long
    @Column(columnDefinition = "TEXT")
    private String message;

    // This is the code we give back to the user (like SVX-AB12CD34)
    // unique = true means no two complaints can have the same tracking ID
    @Column(unique = true)
    private String trackingId;

    // Status of the complaint: SUBMITTED, IN_REVIEW, or RESOLVED
    // We store it as a string in the database (not a number)
    @Enumerated(EnumType.STRING)
    private Status status;

    // Category the user chose: Academic, Hostel, Misconduct, Other
    private String category;

    // Notes added by admin (optional, can be empty)
    @Column(columnDefinition = "TEXT")
    private String adminNotes;

    // When was this complaint submitted
    private LocalDateTime createdAt;

    // This method runs automatically BEFORE the data is saved to DB
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();   // set current time
        this.status = Status.SUBMITTED;          // always start as SUBMITTED
    }

    // This is the Enum (a fixed list of allowed values) for status
    public enum Status {
        SUBMITTED,   // just came in
        IN_REVIEW,   // admin is looking at it
        RESOLVED     // done
    }

    // --- Getters and Setters (boring but necessary) ---
    // Viva tip: "Getters/Setters let other classes read and change the fields"

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getTrackingId() { return trackingId; }
    public void setTrackingId(String trackingId) { this.trackingId = trackingId; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getAdminNotes() { return adminNotes; }
    public void setAdminNotes(String adminNotes) { this.adminNotes = adminNotes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}