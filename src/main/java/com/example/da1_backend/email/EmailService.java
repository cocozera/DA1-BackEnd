package com.example.da1_backend.email;

import brevo.*;
import brevo.auth.*;
import brevoModel.*;
import brevoApi.TransactionalEmailsApi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
public class EmailService {

    @Value("${brevo.api.key}")
    private String apiKey;

    @Value("${brevo.sender.email}")
    private String senderEmail;

    @Value("${brevo.sender.name}")
    private String senderName;

    public void sendVerificationEmail(String toEmail, String code) {
        try {
            // Configurar API Client con la API Key
            ApiClient defaultClient = Configuration.getDefaultApiClient();
            ApiKeyAuth apiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("api-key");
            apiKeyAuth.setApiKey(apiKey);

            TransactionalEmailsApi apiInstance = new TransactionalEmailsApi();

            // Crear el email
            SendSmtpEmail sendEmail = new SendSmtpEmail();
            sendEmail.setTo(Collections.singletonList(new SendSmtpEmailTo().email(toEmail)));
            sendEmail.setSender(new SendSmtpEmailSender().email(senderEmail).name(senderName));
            sendEmail.setSubject("Tu código de verificación");
            sendEmail.setHtmlContent("<h1>Tu código de verificación es: " + code + "</h1>");

            // Enviar el email
            apiInstance.sendTransacEmail(sendEmail);

            System.out.println("Correo enviado exitosamente a: " + toEmail);
        } catch (ApiException e) {
            System.err.println("Error al enviar email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

