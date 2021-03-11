package no.digdir.minidnotificationserver.service;

import no.digdir.minidnotificationserver.api.notification.AuthorizationNotificationEntity;
import no.digdir.minidnotificationserver.config.CacheConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

@Configuration
@Service
public class NotificationCache  {

    final private Cache loginAttemptCache;


    @Autowired
    public NotificationCache(CacheManager cacheManager, CacheConfiguration cacheConfiguration) {
        this.loginAttemptCache = cacheManager.getCache(cacheConfiguration.getCacheNameLoginAttempts());

    }

    public void putLoginAttempt(String key, AuthorizationNotificationEntity notificationEntity) {
        loginAttemptCache.put(key, notificationEntity);
    }

    public AuthorizationNotificationEntity getApprovalNotificationForLoginAttempt(String key) {
        Cache.ValueWrapper valueWrapper = loginAttemptCache.get(key);
        return valueWrapper != null ? (AuthorizationNotificationEntity) valueWrapper.get() : null;
    }

    public void removeLoginAttemptId(String loginAttemptId) {
        loginAttemptCache.evictIfPresent(loginAttemptId);
    }

}