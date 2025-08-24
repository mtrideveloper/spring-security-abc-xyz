package com.mtri.auths.controller;

import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.mtri.auths.util.PathConstants;

@Controller
public class OttCaptchaProxyController {

    @PostMapping(PathConstants.RECAPTCHAV2GG)
    public String verifyCaptchaAndForward(
            @RequestParam String username,
            @RequestParam("g-recaptcha-response") String captchaToken,
            Model model) {

        if (!verifyCaptcha(captchaToken)) {
            model.addAttribute("error", "Captcha không hợp lệ");
            System.out.println("captcha token invalid: "+captchaToken);
            
            return PathConstants.OTT_REQUEST_PATH; // Trả lại trang login
        }
        System.out.println("captcha token: " + captchaToken);

        // Captcha OK: render một view chứa form tự động submit POST đến /ott/gen
        model.addAttribute("username", username);
        return "redirect-ott-form";
    }

    /**
     * ???
     * 
     * @param token
     * @return
     */
    private boolean verifyCaptcha(String token) {
        final String SECRET_KEY = System.getProperty("RECAPTCHA_SECRET");
        System.out.println("sk: "+SECRET_KEY);
        String url = "https://www.google.com/recaptcha/api/siteverify";

        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("secret", SECRET_KEY);
        body.add("response", token);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    request,
                    new ParameterizedTypeReference<Map<String, Object>>() {
                    });
            return (Boolean) response.getBody().get("success");
        } catch (Exception e) {
            return false;
        }
    }
}
