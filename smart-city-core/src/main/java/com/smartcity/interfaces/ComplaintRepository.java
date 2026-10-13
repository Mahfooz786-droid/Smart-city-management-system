package com.smartcity.interfaces;

import com.smartcity.model.Complaint;

import java.util.List;

/** Repository contract for {@link Complaint} persistence. */
public interface ComplaintRepository {

    /** Inserts a new complaint. Status defaults to PENDING inside the impl. */
    Complaint save(Complaint complaint);

    /** Returns all complaints for a given citizen, newest first. */
    List<Complaint> findByCitizenId(int citizenId);

    /** Returns all complaints with the citizen name denormalised, newest first. */
    List<Complaint> findAll();

    /** Updates only the status field of the given complaint id. */
    void updateStatus(int complaintId, Complaint.Status newStatus);
}
