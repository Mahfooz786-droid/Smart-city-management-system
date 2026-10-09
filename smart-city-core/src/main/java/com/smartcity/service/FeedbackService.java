package com.smartcity.service;

import com.smartcity.exception.ValidationException;
import com.smartcity.interfaces.FeedbackRepository;
import com.smartcity.model.Feedback;

import java.util.List;
import java.util.Objects;

/**
 * Service-layer wrapper for {@link Feedback}.
 * Enforces the 1..5 rating range before persistence.
 */
public class FeedbackService {

    private final FeedbackRepository feedback;

    public FeedbackService(FeedbackRepository feedback) {
        this.feedback = Objects.requireNonNull(feedback);
    }

    public Feedback submitFeedback(int citizenId, int rating, String comment) {
        Validator.requirePositiveId(citizenId, "Citizen ID");
        Validator.requireRating(rating);
        Validator.requireNonBlank(comment, "Comment");

        Feedback f = new Feedback();
        f.setCitizenId(citizenId);
        f.setRating(rating);
        f.setComment(comment.trim());
        return feedback.save(f);
    }

    public List<Feedback> getAllFeedback() {
        return feedback.findAll();
    }
}
