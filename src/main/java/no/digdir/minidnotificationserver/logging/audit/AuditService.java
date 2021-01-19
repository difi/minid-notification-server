package no.digdir.minidnotificationserver.logging.audit;


import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;
import no.digdir.minidnotificationserver.api.notification.NotificationEntity;
import no.digdir.minidnotificationserver.domain.RegistrationDevice;
import no.digdir.minidnotificationserver.service.AdminContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Service
public class AuditService {

    private final Logger auditLogger = LoggerFactory.getLogger("no.digdir.logging.AuditLog");

    public AuditService() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

        try {
            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(context);
            InputStream auditXml = new ClassPathResource("logback-audit.xml").getInputStream();
            configurator.doConfigure(auditXml);
        } catch (JoranException | IOException je) {
            // StatusPrinter will handle this
        }
        StatusPrinter.printInCaseOfErrorsOrWarnings(context);
    }

    public void auditRegistrationServiceRegisterDevice(RegistrationDevice device) {
        auditLogger.info(
                "Device registered.",
                kv("auditId", AuditID.DEVICE_REGISTER.auditId()),
                kv("device", device)
        );
    }

    public void auditRegistrationServiceUpdateDevice(RegistrationDevice existingDevice, RegistrationDevice updatedDevice) {
        auditLogger.info(
                "Device updated.",
                kv("auditId", AuditID.DEVICE_UPDATE.auditId()),
                kv("oldDevice", existingDevice),
                kv("newDevice", updatedDevice)
        );
    }

    public void auditRegistrationServiceDeleteDevice(RegistrationDevice device) {
        auditLogger.info(
                "Device deleted.",
                kv("auditId", AuditID.DEVICE_DELETE.auditId()),
                kv("device", device)
        );
    }


    public void auditNotificationSend(NotificationEntity notification, AdminContext adminContext) {
        auditLogger.info(
                "Notification sent.",
                kv("auditId", AuditID.NOTIFICATION_SEND.auditId()),
                kv("adminUserId", adminContext.getFullAdminUserId()),
                kv("notification", notification)
        );
    }
}
