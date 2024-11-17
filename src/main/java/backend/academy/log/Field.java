package backend.academy.log;

import java.util.function.BiFunction;

public enum Field {
    AGENT((String value, Log log) -> log.userAgent().contains(value)),
    METHOD((String value, Log log) -> log.request().method().contains(value)),
    CLIENT_IP_ADDRESS((String value, Log log) -> log.clientIpAddress().equals(value)),
    USER_ID((String value, Log log) -> log.userId().equals(value)),
    USERNAME((String value, Log log) -> log.username().equals(value)),
    RESOURCE((String value, Log log) -> log.request().resource().equals(value)),
    HTTP_VERSION((String value, Log log) -> log.request().httpVersion().equals(value)),
    HTTP_STATUS_CODE((String value, Log log) -> log.httpStatusCode() == Integer.parseInt(value)),
    RESPONSE_SIZE((String value, Log log) -> log.responseSize() == Long.parseLong(value)),
    REFERRER((String value, Log log) -> log.referrer().equals(value));

    private final BiFunction<String, Log, Boolean> checkFunction;

    Field(BiFunction<String, Log, Boolean> checkFunction) {
        this.checkFunction = checkFunction;
    }

    public boolean check(String value, Log log) {
        return this.checkFunction.apply(value, log);
    }

}
