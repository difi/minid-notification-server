package no.digdir.minidnotificationserver.api.notification;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import no.digdir.minidnotificationserver.api.internal.approval.RequestApprovalEntity;
import no.digdir.minidnotificationserver.logging.audit.AuditService;
import no.digdir.minidnotificationserver.service.AdminContext;
import no.digdir.minidnotificationserver.service.RequestApprovalCache;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@WebAppConfiguration
@SpringBootTest
public class AppAuthorizationApprovalEndpointTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuditService auditService;

    @MockBean
    private RequestApprovalCache requestApprovalCache;

    @Captor
    private ArgumentCaptor<Message> messageCaptor;

    @MockBean
    public JwtDecoder jwtDecoder;

    @Autowired
    private WebApplicationContext wac;

    @Test
    public void approvalTest() throws Exception {
        String token = "testabc";
        String input = "{ \"request_id\": \"request-id_1\"}";
        Jwt testJWT = getJwt("person_id", "minid:app.register");
        when(jwtDecoder.decode(token)).thenReturn(testJWT);
        when(requestApprovalCache.getApprovalRequestForLoginAttempt("request-id_1")).thenReturn(getApprovalRequest());

        ResultActions resultActions = mockMvc.perform(post("/api/authorization/approve")
                .header("Authorization", "Bearer " + token)
                .header("Client-id", "")
                .header("Client-secret", "")
                .content(input)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .with(csrf())
        )
                .andExpect(status().isUnauthorized());

    }

    private RequestApprovalEntity getApprovalRequest() {
        return RequestApprovalEntity.builder()
                .person_identifier("person_identifier")
                .login_attempt_id("request-id")
                .login_attempt_counter(1)
                .service_provider("Supperådet")
                .build();
    }

    Jwt getJwt(String pid, String scope) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("pid", pid);
        claims.put("scope", scope);
        Map<String, Object> headers = new HashMap<>();
        headers.put("testheader", "test");
        return new Jwt("lol", Instant.now(), Instant.now().plusSeconds(50L), headers, claims);
    }


}
