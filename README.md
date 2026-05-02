# 🛡️ Secure Voice — Anonymous Reporting System

A privacy-first complaint management system built with Spring Boot + MySQL +  JS.
```

### Access the App
| Page              | URL                                       |
|-------------------|-------------------------------------------|
| Submit Complaint  | http://localhost:8080/index.html          |
| Track Complaint   | http://localhost:8080/track.html          |
| Admin Login       | http://localhost:8080/admin/login         |
| Admin Dashboard   | http://localhost:8080/admin/dashboard.html|

**Default Admin Credentials:**
- Username: `admin`
- Password: `Admin@123`

## 🧪 Testing with Postman

### Submit a Complaint
```
POST http://localhost:8080/complaint
Content-Type: application/json

{
  "message": "This is a test complaint about hostel facilities.",
  "category": "Hostel"
}
```

### Track a Complaint
```
GET http://localhost:8080/complaint/SVX-A1B2C3D4
```

### Admin — Get All (requires session cookie from browser login)
```
GET http://localhost:8080/admin/complaints
```

### Admin — Update Status
```
PUT http://localhost:8080/admin/update-status
Content-Type: application/json

{
  "complaintId": 1,
  "newStatus": "RESOLVED",
  "adminNotes": "Issue addressed. Warden informed."
}
```

## 🔮 Future Scope (Not Implemented)
- AI-based auto-classification of complaint category
- Email notifications to admin on new submission
- ERP/LMS integration
- Mobile application (React Native / Flutter)
- Redis-based persistent rate limiting
- Multi-admin support with role management
