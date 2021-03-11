package no.digdir.minidnotificationserver.integration.minidEid;

import lombok.AllArgsConstructor;
import no.digdir.minidnotificationserver.config.CorrelationIdInterceptor;
import no.digdir.minidnotificationserver.config.developer.DeveloperLoggingRequestInterceptor;
import no.digdir.minidnotificationserver.integration.google.GoogleClientErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@AllArgsConstructor
@Component
public class ApiRestTemplate extends RestTemplate {

    public ApiRestTemplate(@Autowired(required = false) DeveloperLoggingRequestInterceptor loggingInterceptor,
                              CorrelationIdInterceptor correlationIdInterceptor,
                              GoogleClientErrorHandler responseErrorHandler) {
        this.setRequestFactory(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
        this.setErrorHandler(responseErrorHandler);
        this.getInterceptors().add(correlationIdInterceptor);
        if(loggingInterceptor != null) { // only enabled in debug profile
            this.getInterceptors().add(loggingInterceptor);
        }
    }
}
