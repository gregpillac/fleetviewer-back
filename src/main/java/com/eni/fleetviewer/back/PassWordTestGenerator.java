package com.eni.fleetviewer.back;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PassWordTestGenerator {

        public static void main(String[] args) {
            PasswordEncoder encoder = new BCryptPasswordEncoder();
            String hashed = encoder.encode("admin");
            System.out.println("Mot de passe hashé : " + hashed);
        }
}
