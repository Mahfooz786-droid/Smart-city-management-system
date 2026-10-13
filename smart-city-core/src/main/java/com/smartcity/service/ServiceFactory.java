package com.smartcity.service;

import com.smartcity.config.DatabaseConfig;
import com.smartcity.dao.JdbcCitizenRepository;
import com.smartcity.dao.JdbcComplaintRepository;
import com.smartcity.dao.JdbcFeedbackRepository;
import com.smartcity.dao.JdbcServiceRepository;

/**
 * Convenience factory that wires up all the JDBC repositories and the
 * service layer in one call.
 *
 * <p>Both the GUI and Web modules use this so they don't have to repeat
 * the wiring boilerplate. The returned instance is a small immutable
 * holder — pass it around rather than building services by hand.
 */
public final class ServiceFactory {

    private final CitizenService    citizenService;
    private final ComplaintService  complaintService;
    private final CityServiceService cityServiceService;
    private final FeedbackService   feedbackService;
    private final CitizenAuthService citizenAuthService;

    public ServiceFactory(DatabaseConfig db) {
        JdbcCitizenRepository    citizens   = new JdbcCitizenRepository(db);
        JdbcComplaintRepository  complaints = new JdbcComplaintRepository(db);
        JdbcServiceRepository    services   = new JdbcServiceRepository(db);
        JdbcFeedbackRepository   feedback   = new JdbcFeedbackRepository(db);

        this.citizenService      = new CitizenService(citizens);
        this.complaintService    = new ComplaintService(complaints);
        this.cityServiceService  = new CityServiceService(services);
        this.feedbackService     = new FeedbackService(feedback);
        this.citizenAuthService  = new CitizenAuthService(citizens);
    }

    public CitizenService       citizenService()       { return citizenService; }
    public ComplaintService     complaintService()     { return complaintService; }
    public CityServiceService   cityServiceService()   { return cityServiceService; }
    public FeedbackService      feedbackService()      { return feedbackService; }
    public CitizenAuthService   citizenAuthService()   { return citizenAuthService; }
}
