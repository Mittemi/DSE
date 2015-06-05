package opPlanner.OPmatcher.Service;

/**
 * Created by Thomas on 05.06.2015.
 */
public class OPMatcherServicesNotAvailableException extends RuntimeException {

    public OPMatcherServicesNotAvailableException() {
        super("OPMatcher cannot be used now. Required data is currently not available in the underlying data storage.");
    }
}
