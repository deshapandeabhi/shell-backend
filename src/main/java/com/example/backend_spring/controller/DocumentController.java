package com.example.backend_spring.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/documents")
public class DocumentController {

    // For production, this should be an external path or S3 bucket
    // For now, we point to the resources folder where we moved the files
    private final String documentBaseDir = "src/main/resources/documents";

    /**
     * Securely serves a document file.
     * VAPT Compliance: 
     * 1. Path Traversal protection (sanitizes filename)
     * 2. X-Content-Type-Options: nosniff
     * 3. Correct MIME types
     * 4. Content-Disposition: inline (opens in new tab)
     */
    @GetMapping("/{category}/{filename}")
    public ResponseEntity<Resource> getDocument(
            @PathVariable String category,
            @PathVariable String filename) {
        
        try {
            // 1. Path Traversal Protection: Sanitize input
            // We only allow alphanumeric, dots, and hyphens. No ".." allowed.
            if (!isValidPath(category) || !isValidPath(filename)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            Path filePath = Paths.get(documentBaseDir).resolve(category).resolve(filename).normalize();
            
            // Double check that the resulting path is still within the base directory
            if (!filePath.startsWith(Paths.get(documentBaseDir).toAbsolutePath())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                String contentType = determineContentType(filename);
                
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .header("X-Content-Type-Options", "nosniff")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Basic path sanitization for VAPT
     */
    private boolean isValidPath(String path) {
        if (path == null || path.isEmpty()) return false;
        // Allow only safe characters. Disallow ".."
        return path.matches("^[a-zA-Z0-9._-]+$") && !path.contains("..");
    }

    private String determineContentType(String filename) {
        if (filename.toLowerCase().endsWith(".pdf")) return "application/pdf";
        if (filename.toLowerCase().endsWith(".docx")) return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        if (filename.toLowerCase().endsWith(".doc")) return "application/msword";
        if (filename.toLowerCase().endsWith(".jpg") || filename.toLowerCase().endsWith(".jpeg")) return "image/jpeg";
        if (filename.toLowerCase().endsWith(".png")) return "image/png";
        return "application/octet-stream";
    }
}
