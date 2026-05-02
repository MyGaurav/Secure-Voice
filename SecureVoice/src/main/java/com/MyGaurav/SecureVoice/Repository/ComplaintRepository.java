package com.MyGaurav.SecureVoice.Repository;

import com.MyGaurav.SecureVoice.Entity.Complaints;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// @Repository = this class talks to the database
// JpaRepository<Complaint, Long> gives us free methods like:
//   save(), findById(), findAll(), deleteById()
// We get all of this WITHOUT writing any SQL!
@Repository
public interface ComplaintRepository extends JpaRepository<Complaints, Long> {

    // Spring reads the method NAME and writes the SQL for us automatically!
    // This becomes: SELECT * FROM complaint WHERE tracking_id = ?
    Optional<Complaints> findByTrackingId(String trackingId);

    // This becomes: SELECT * FROM complaint ORDER BY created_at DESC
    List<Complaints> findAllByOrderByCreatedAtDesc();

    // This becomes: SELECT COUNT(*) > 0 FROM complaint WHERE tracking_id = ?
    // Used to make sure our generated IDs are always unique
    boolean existsByTrackingId(String trackingId);
}
