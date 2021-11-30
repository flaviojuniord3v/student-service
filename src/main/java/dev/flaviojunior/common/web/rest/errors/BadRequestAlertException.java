package dev.flaviojunior.common.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class BadRequestAlertException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    private final String entityName;

    public BadRequestAlertException(String defaultMessage, String entityName) {
        this(ErrorConstants.DEFAULT_TYPE, defaultMessage, entityName);
    }

    public BadRequestAlertException(URI type, String defaultMessage, String entityName) {
        super(type, defaultMessage, Status.BAD_REQUEST, null, null, null, getAlertParameters(entityName));
        this.entityName = entityName;
    }

    private static Map<String, Object> getAlertParameters(String entityName) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("params", entityName);
        return parameters;
    }

    public String getEntityName() {
        return entityName;
    }


}
