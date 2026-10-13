package com.smartcity.security;

import at.favre.lib.crypto.bcrypt.BCrypt;

/**
 * Thin wrapper around the BCrypt library from
 * {@code at.favre.lib.crypto:bcrypt}.
 *
 * <p>Why BCrypt?
 * <ul>
 *   <li>Adaptive cost factor — keeps pace with hardware improvements.</li>
 *   <li>Built-in per-hash salt — no need to manage a separate salt column.</li>
 *   <li>Constant-time comparison — guards against timing side-channels.</li>
 * </ul>
 *
 * <p>This class is the ONLY place in the codebase that knows how passwords
 * are hashed. Switching to Argon2 later means changing one file, not every
 * service.
 */
public final class PasswordHasher {

    /** Cost factor 12 ≈ ~250 ms per hash on a 2024-era CPU; tunable. */
    private static final int COST = 12;

    private PasswordHasher() {}

    /**
     * Hashes a plaintext password into a BCrypt hash suitable for storage.
     *
     * @throws IllegalArgumentException if the password is null or empty
     *                                  (we never store empty passwords).
     */
    public static String hash(String plaintextPassword) {
        if (plaintextPassword == null || plaintextPassword.isEmpty()) {
            throw new IllegalArgumentException("Password must not be empty.");
        }
        return BCrypt.with(BCrypt.Version.VERSION_2A)
                     .hashToString(COST, plaintextPassword.toCharArray());
    }

    /**
     * Verifies a plaintext candidate against a stored BCrypt hash in
     * constant time.
     */
    public static boolean verify(String plaintextCandidate, String storedHash) {
        if (storedHash == null || storedHash.isEmpty()) return false;
        return BCrypt.verifyer()
                     .verify(plaintextCandidate.toCharArray(), storedHash)
                     .verified;
    }
}
