package com.bcd.parser.impl.someip.data;

public enum ReturnCode {
    /**
     * No error occurred
     */
    E_OK(0x00),

    /**
     * An unspecified error occurred
     */
    E_NOT_OK(0x01),

    /**
     * The requested Service ID is unknown.
     */
    E_UNKNOWN_SERVICE(0x02),

    /**
     * The requested Method ID is unknown. Service ID is
     * known.
     */
    E_UNKNOWN_METHOD(0x03),

    /**
     * Service ID and Method ID are known. Application
     * not running.
     */
    E_NOT_READY(0x04),

    /**
     * System running the service is not reachable (internal error code only).
     */
    E_NOT_REACHABLE(0x05),

    /**
     * A timeout occurred (internal error code only).
     */
    E_TIMEOUT(0x06),

    /**
     * Version of SOME/IP protocol not supported
     */
    E_WRONG_PROTOCOL_VERSION(0x07),

    /**
     * Interface version mismatch
     */
    E_WRONG_INTERFACE_VERSION(0x08),

    /**
     * Deserialization error, so that payload cannot be deserialized.
     */
    E_MALFORMED_MESSAGE(0x09),

    /**
     * An unexpected message type was received (e.g.
     * REQUEST_NO_RETURN for a method defined as
     * REQUEST).
     */
    E_WRONG_MESSAGE_TYPE(0x0a),

    /**
     * Repeated E2E calculation error
     */
    E_E2E_REPEATED(0x0b),

    /**
     * Wrong E2E sequence error
     */
    E_E2E_WRONG_SEQUENCE(0x0c),

    /**
     * Not further specified E2E error
     */
    E_E2E(0x0d),

    /**
     * E2E not available
     */
    E_E2E_NOT_AVAILABLE(0x0e),

    /**
     * No new data for E2E calculation present.
     */
    E_E2E_NO_NEW_DATA(0x0f);

    private final int val;


    public int getVal() {
        return val;
    }

    ReturnCode(int val) {
        this.val = val;
    }

    static final ReturnCode[] arr =new ReturnCode[0x0f+1];

    static {
        for (ReturnCode data : ReturnCode.values()) {
            arr[data.val]=data;
        }
    }

    public static ReturnCode valueOf(int val) {
        return arr[val];
    }
}
