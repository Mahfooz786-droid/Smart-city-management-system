package com.smartcity.service;

import com.smartcity.exception.AuthorizationException;
import com.smartcity.exception.NotFoundException;
import com.smartcity.exception.ValidationException;
import com.smartcity.interfaces.ComplaintRepository;
import com.smartcity.model.Complaint;

import java.util.List;
import java.util.Objects;

/**
 * Service-layer CRUD for {@link Complaint}.
 *
 * <p>Authorization rule: a citizen may only read / write their own
 * complaints. {@link #registerComplaint} and {@link #getComplaintsForCitizen}
 * enforce this by accepting the caller's citizenId and never trusting a
 * user-supplied citizenId for someone else's record.
 */
public class ComplaintService {

    private final ComplaintRepository complaints;

    public ComplaintService(ComplaintRepository complaints) {
        this.complaints = Objects.requireNonNull(complaints);
    }

    /**
     * Registers a new complaint on behalf of {@code citizenId}.
     *
     * @return the saved {@link Complaint} with id + status set
     */
    public Complaint registerComplaint(int citizenId, String subject, String description) {
        Validator.requirePositiveId(citizenId, "Citizen ID");
        Validator.requireNonBlank(subject, "Subject");
        Validator.requireNonBlank(description, "Description");

        Complaint c = new Complaint();
        c.setCitizenId(citizenId);
        c.setSubject(subject.trim());
        c.setDescription(description.trim());
        c.setStatus(Complaint.Status.PENDING);
        return complaints.save(c);
    }

    /**
     * Returns only the complaints that belong to the calling citizen.
     * Used by the citizen-facing dashboard.
     */
    public List<Complaint> getComplaintsForCitizen(int citizenId) {
        Validator.requirePositiveId(citizenId, "Citizen ID");
        return complaints.findByCitizenId(citizenId);
    }

    /**
     * Returns all complaints with the citizen name denormalised.
     * Used by the admin dashboard.
     */
    public List<Complaint> getAllComplaints() {
        return complaints.findAll();
    }

    /**
     * Updates the status of a complaint. Admin-only — caller must pass
     * {@code isAdmin = true} or the call is rejected.
     *
     * @throws AuthorizationException if the caller is not an admin
     * @throws NotFoundException       if no such complaint exists
     */
    public void updateStatus(int complaintId, Complaint.Status newStatus, boolean isAdmin) {
        if (!isAdmin) {
            throw new AuthorizationException("Only administrators may change complaint status.");
        }
        Validator.requirePositiveId(complaintId, "Complaint ID");
        complaints.updateStatus(complaintId, newStatus);
    }
}
