package com.bcd.support_parser.impl.someip.data;

public enum MessageType {
    /**
     * A request expecting a response (even void)
     */
    REQUEST(0x00),

    /**
     * A fire&forget request
     */
    REQUEST_NO_RETURN(0x01),

    /**
     * A request of a notification/event callback
     * expecting no response
     */
    NOTIFICATION(0x02),

    /**
     * The response message
     */
    RESPONSE(0x80),

    /**
     * The response containing an error
     */
    ERROR(0x81),

    /**
     * A TP request expecting a response (even
     * void)
     */
    TP_REQUEST(0x20),

    /**
     * A TP fire&forget request
     */
    TP_REQUEST_NO_RETURN(0x21),

    /**
     * A TP request of a notification/event callback expecting no response
     */
    TP_NOTIFICATION(0x22),

    /**
     * The TP response message
     */
    TP_RESPONSE(0xa0),

    /**
     * The TP response containing an error
     */
    TP_ERROR(0xa1);

    private final int val;

    MessageType(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }

    static final MessageType[] arr =new MessageType[0xa1+1];

    static {
        for (MessageType data : MessageType.values()) {
            arr[data.val]=data;
        }
    }

    public static MessageType valueOf(int val) {
        return arr[val];
    }

}
