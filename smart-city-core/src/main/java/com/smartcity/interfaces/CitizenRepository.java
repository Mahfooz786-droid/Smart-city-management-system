package com.smartcity.interfaces;

import com.smartcity.model.Citizen;

import java.util.List;
import java.util.Optional;

/**
 * Repository contract for {@link Citizen} persistence.
 *
 * <p>Defining this interface lets the service layer depend on the
 * abstraction, not the concrete JDBC implementation. The same service can
 * be unit-tested with an in-memory fake repository, then handed a real
 * {@code JdbcCitizenRepository} in production.
 *
 * @param <T> the entity type stored by the repository
 */
public interface CitizenRepository {

    /**
     * Inserts a new citizen record. The {@code passwordHash} field on the
     * supplied {@link Citizen} is expected to be a BCrypt hash; this
     * method never accepts a plaintext password.
     *
     * @return the newly created citizen, with its generated id populated.
     */
    Citizen save(Citizen citizen);

    /** Returns the citizen with the given id, or empty if not found. */
    Optional<Citizen> findById(int id);

    /**
     * Returns the citizen matching the given email, or empty. Used by the
     * auth flow to look up the row before BCrypt verification.
     */
    Optional<Citizen> findByEmail(String email);

    /** Returns every citizen in the database, ordered by id ascending. */
    List<Citizen> findAll();

    /** Updates name/email/phone/address of the citizen with the given id. */
    void update(Citizen citizen);

    /** Deletes the citizen with the given id (cascades to complaints/feedback). */
    void deleteById(int id);
}
