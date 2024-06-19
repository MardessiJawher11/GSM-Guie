package gsm.service;

import java.io.IOException;
import java.net.HttpURLConnection;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.ResourceAccessException;
@Service
public class SmsService {
    
    private static final String SMS_API_URL = "http://192.168.0.200:8082";
    private static final String AUTHORIZATION_KEY = "2d9d4188-a1b0-4bac-9d04-cb2af8f7186d";
    
    private final RestTemplate restTemplate;
    
    public SmsService() {
        this.restTemplate = new RestTemplate();
        // Set custom timeout values if necessary
        int timeout = 5000; // 5 seconds
        restTemplate.setRequestFactory(new SimpleClientHttpRequestFactory() {
            @Override
            protected void prepareConnection(HttpURLConnection connection, String httpMethod) throws IOException {
                super.prepareConnection(connection, httpMethod);
                connection.setConnectTimeout(timeout);
                connection.setReadTimeout(timeout);
            }
        });
    }
    
    public void sendSms(String to, String message) {
        SmsRequest smsRequest = new SmsRequest(to, message);
    
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", AUTHORIZATION_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON);
    
        HttpEntity<SmsRequest> request = new HttpEntity<>(smsRequest, headers);
    
        try {
            restTemplate.postForObject(SMS_API_URL, request, String.class);
        } catch (ResourceAccessException e) {
            // Log the exception and handle it accordingly
            System.err.println("Failed to send SMS: " + e.getMessage());
            // Optionally, you can throw a custom exception or handle it in another way
        }
    }
    
    static class SmsRequest {
        private String to;
        private String message;
    
        public SmsRequest(String to, String message) {
            this.to = to;
            this.message = message;
        }
    
        // Getters and setters
        public String getTo() { return to; }
        public void setTo(String to) { this.to = to; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}
