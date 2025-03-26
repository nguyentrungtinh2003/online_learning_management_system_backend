package com.TrungTinhBackend.codearena_backend.Config;

import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class PayPalConfig {

    @Value("${paypal.client.id}")
    private String clientId;

    @Value("${paypal.client.secret}")
    private String clientSecret;

    @Value("${paypal.mode}")
    private String mode;

    public PayPalConfig(String clientId, String clientSecret, String mode) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.mode = mode;
    }

    @Bean
    public APIContext apiContext() throws PayPalRESTException {
        return new APIContext(new OAuthTokenCredential(clientId, clientSecret, getPayPalSdkConfig()).getAccessToken());
    }

    private Map<String, String> getPayPalSdkConfig() {
        Map<String, String> configMap = new HashMap<>();
        configMap.put("mode", mode);
        return configMap;
    }
}
