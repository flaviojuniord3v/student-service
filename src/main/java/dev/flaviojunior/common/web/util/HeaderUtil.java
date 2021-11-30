package dev.flaviojunior.common.web.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Utility class for HTTP headers creation.
 */
public final class HeaderUtil {

    private static final Logger log = LoggerFactory.getLogger(HeaderUtil.class);

    private HeaderUtil() {
    }

    /**
     * <p>
     * createAlert.
     * </p>
     *
     * @param applicationName a {@link String} object.
     * @param message         a {@link String} object.
     * @param param           a {@link String} object.
     * @return a {@link HttpHeaders} object.
     */
    public static HttpHeaders createAlert(String applicationName, String message, String param) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-" + applicationName + "-alert", message);
        try {
            headers.add("X-" + applicationName + "-params",
                    URLEncoder.encode(param, StandardCharsets.UTF_8.toString()));
        } catch (UnsupportedEncodingException e) {
            // StandardCharsets are supported by every Java implementation so this exception
            // will never happen
        }
        return headers;
    }

    /**
     * <p>
     * createEntityCreationAlert.
     * </p>
     *
     * @param applicationName a {@link String} object.
     * @param entityName      a {@link String} object.
     * @param param           a {@link String} object.
     * @return a {@link HttpHeaders} object.
     */
    public static HttpHeaders createEntityCreationAlert(String applicationName, String entityName, String param) {
        String message = entityName + " inserido com sucesso ";
        return createAlert(applicationName, message, param);
    }

    /**
     * <p>
     * createEntityUpdateAlert.
     * </p>
     *
     * @param applicationName a {@link String} object.
     * @param entityName      a {@link String} object.
     * @param param           a {@link String} object.
     * @return a {@link HttpHeaders} object.
     */
    public static HttpHeaders createEntityUpdateAlert(String applicationName, String entityName, String param) {
        String message = entityName + " alterado com sucesso ";
        return createAlert(applicationName, message, param);
    }

    /**
     * <p>
     * createEntityDeletionAlert.
     * </p>
     *
     * @param applicationName a {@link String} object.
     * @param entityName      a {@link String} object.
     * @param param           a {@link String} object.
     * @return a {@link HttpHeaders} object.
     */
    public static HttpHeaders createEntityDeletionAlert(String applicationName, String entityName, String param) {
        String message = entityName + " removido com sucesso ";
        return createAlert(applicationName, message, param);
    }

    /**
     * <p>
     * createFailureAlert.
     * </p>
     *
     * @param applicationName a {@link String} object.
     * @param entityName      a {@link String} object.
     * @param message         a {@link String} object.
     * @return a {@link HttpHeaders} object.
     */
    public static HttpHeaders createFailureAlert(String applicationName, String entityName, String message) {
        log.error("Entity processing failed, {}", message);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-" + applicationName + "-error", message);
        headers.add("X-" + applicationName + "-params", entityName);
        return headers;
    }
}
