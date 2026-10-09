package com.smartcity.interfaces;

import com.smartcity.model.Feedback;

import java.util.List;

/** Repository contract for {@link Feedback} persistence. */
public interface FeedbackRepository {

    /** Inserts a new feedback record. */
    Feedback save(Feedback feedback);

    /** Returns all feedback entries with the citizen name denormalised, newest first. */
    List<Feedback> findAll();
}
