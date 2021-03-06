package no.digdir.minidnotificationserver.logging.audit;

import no.idporten.logging.audit.AuditIdentifier;

public enum AuditID implements AuditIdentifier {

    DEVICE_REGISTER(1, "DEVICE-REGISTER"),
    DEVICE_UPDATE(2, "DEVICE-UPDATE"),
    DEVICE_DELETE(3, "DEVICE-DELETE"),

    APNS_TOKEN_IMPORT(7, "APNS-TOKEN-IMPORT"),

    VALIDATE_PID_TOKEN(8, "VALIDATE-PID-TOKEN"),

    NOTIFICATION_SEND(10, "NOTIFICATION-SEND");

    static final String AUDIT_ID_FORMAT = "MINID-NOTIFICATION-%d-%s";
    final String auditId;
    final int id;

    AuditID(int numericId, String stringId) {
        this.auditId = String.format(AUDIT_ID_FORMAT, numericId, stringId);
        this.id = numericId;
    }

    public int id() {
        return id;
    }

    public String auditId() {
        return auditId;
    }

}
