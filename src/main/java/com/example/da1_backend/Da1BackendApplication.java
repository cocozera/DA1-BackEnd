package com.example.da1_backend;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Da1BackendApplication {

    public static void main(String[] args) {
        // Cargar variables de entorno desde .env
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

        System.setProperty("DB_URL", dotenv.get("DB_URL"));
        System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
        System.setProperty("JWT_SECRET", dotenv.get("JWT_SECRET"));
        System.setProperty("BREVO_APIKEY", dotenv.get("BREVO_APIKEY"));
        System.setProperty("BREVO_SENDER_EMAIL", dotenv.get("BREVO_SENDER_EMAIL"));
        System.setProperty("BREVO_SENDER_NAME", dotenv.get("BREVO_SENDER_NAME"));


        SpringApplication.run(Da1BackendApplication.class, args);
    }
}
