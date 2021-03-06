package no.digdir.minidnotificationserver.service;

import lombok.RequiredArgsConstructor;
import no.digdir.minidnotificationserver.api.validate.ValidateEntity;
import no.digdir.minidnotificationserver.logging.audit.AuditService;
import no.digdir.minidnotificationserver.repository.RegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class ValidationService {

    private final RegistrationRepository registrationRepository;
    private final AuditService auditService;


    public boolean validate(ValidateEntity validateEntity, AdminContext adminContext) {
        boolean result = registrationRepository.findByFcmTokenOrApnsToken(validateEntity.getPerson_identifier(), validateEntity.getToken());
        auditService.auditValidatePidToken(validateEntity, result, adminContext);
        return result;
    }
}
