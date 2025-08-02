package com.mtri.auths.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
public class ImageProxyController {

    /**
     * Tải ảnh như một trình duyệt (nhưng phía backend, không bị CORS hay privacy
     * policy chặn).
     */
    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/proxy-image")
    public ResponseEntity<byte[]> proxyImage(@RequestParam("url") String imageUrl) {
        // Chặn các domain không phải Google (tùy chọn bảo mật)
        if (!imageUrl.startsWith("https://lh3.googleusercontent.com/")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            ResponseEntity<byte[]> response = restTemplate.exchange(
                    imageUrl,
                    HttpMethod.GET,
                    null,
                    byte[].class);

            HttpHeaders headers = new HttpHeaders();
            String lowerUrl = imageUrl.toLowerCase();
            MediaType contentType;
            if (lowerUrl.endsWith(".png")) 
                contentType = MediaType.IMAGE_PNG;
             else if (lowerUrl.endsWith(".gif")) 
                contentType = MediaType.IMAGE_GIF;
            else 
                contentType = MediaType.IMAGE_JPEG;
            headers.setContentType(contentType);
            
            return new ResponseEntity<>(response.getBody(), headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        }
    }
}
