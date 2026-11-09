package com.aimbitious.job.security;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class GoogleTokenVerifier {

    @Value("${google.client-id}")
    private String clientId;

    public GoogleIdToken.Payload verify(String idToken)
            throws GeneralSecurityException, IOException {

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(),
                GsonFactory.getDefaultInstance()
        )
                .setAudience(Collections.singletonList(clientId))
                .build();

        GoogleIdToken googleIdToken = verifier.verify(idToken);

        if (googleIdToken == null) {
            return null;
        }

        return googleIdToken.getPayload();
    }
}