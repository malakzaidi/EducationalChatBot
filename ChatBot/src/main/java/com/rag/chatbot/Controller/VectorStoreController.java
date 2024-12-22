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

    @Autowired
    public VectorStoreController(RagDataLoader ragDataLoader) {
        this.ragDataLoader = ragDataLoader;
    }

    @PostMapping("/upload-pdf")
    public ResponseEntity<String> uploadPdf(@RequestParam("file") MultipartFile file) {
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
