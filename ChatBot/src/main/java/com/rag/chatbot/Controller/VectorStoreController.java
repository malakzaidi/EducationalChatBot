package com.rag.chatbot.Controller;


import com.rag.chatbot.Service.RagDataLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/vector-store")
public class VectorStoreController {
    private final RagDataLoader ragDataLoader;
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "password";
    private static final String ADMIN_TOKEN = "valid-admin-token";

    @Autowired
    public VectorStoreController(RagDataLoader ragDataLoader) {
        this.ragDataLoader = ragDataLoader;
    }

    @PostMapping("/admin-login")
    public ResponseEntity<String> adminLogin(@RequestParam String username, @RequestParam String password) {
        if (ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password)) {
            return ResponseEntity.ok(ADMIN_TOKEN);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password.");
    }

    @PostMapping("/upload-pdf")
    public ResponseEntity<String> uploadPdf(
            @RequestParam("file") MultipartFile file,
            @RequestParam("token") String token) {
        if (!ADMIN_TOKEN.equals(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized: Invalid admin token.");
        }

        try {
            if (!file.getContentType().equals("application/pdf")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Only PDF files are allowed.");
            }

            ByteArrayResource resource = new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            };

            // Process and add the PDF to the vector store
            ragDataLoader.addPdfToVectorStore(resource);

            return ResponseEntity.ok("PDF successfully uploaded and added to the vector store.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process the PDF file.");
        }
    }
}

