package com.eni.fleetviewer.back.controller;

import com.eni.fleetviewer.back.dto.ContactRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contact")
@CrossOrigin(origins = { "http://localhost:4300", "https://fleetviewer.onrender.com" })
public class ContactController {

    private final JavaMailSender mailSender;

    public ContactController(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @PostMapping
    public ResponseEntity<?> sendContactMail(@RequestBody ContactRequestDTO request) {

        // Vérification des champs obligatoires
        if (request.getName() == null || request.getEmail() == null ||
                request.getSubject() == null || request.getMessage() == null) {
            return ResponseEntity.badRequest().body("Tous les champs sont obligatoires.");
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("neosolix.group@gmail.com"); // TODO <- remplace par le mail de réception
            message.setSubject("[Contact Site] " + request.getSubject());
            message.setText(
                    "Nom: " + request.getName() + "\n" +
                            "Email: " + request.getEmail() + "\n\n" +
                            "Message:\n" + request.getMessage());
            message.setReplyTo(request.getEmail());

            mailSender.send(message);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erreur lors de l’envoi du mail.");
        }
    }
}
