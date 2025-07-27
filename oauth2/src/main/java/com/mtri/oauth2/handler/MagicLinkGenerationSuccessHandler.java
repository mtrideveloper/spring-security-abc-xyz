package com.mtri.oauth2.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.ott.OneTimeToken;
import org.springframework.security.web.authentication.ott.OneTimeTokenGenerationSuccessHandler;
import org.springframework.security.web.authentication.ott.RedirectOneTimeTokenGenerationSuccessHandler;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.mtri.oauth2.util.PathConstants;

import java.io.IOException;

@Component
public class MagicLinkGenerationSuccessHandler implements OneTimeTokenGenerationSuccessHandler {

    @Value("${spring.mail.username}")
    private String sender;
    private final JavaMailSender mailSender;

    private final OneTimeTokenGenerationSuccessHandler redirectHandler =
            new RedirectOneTimeTokenGenerationSuccessHandler(PathConstants.OTT_SENT_PATH);

    public MagicLinkGenerationSuccessHandler(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            OneTimeToken oneTimeToken) throws IOException, ServletException {
        UriComponentsBuilder builder =
                UriComponentsBuilder.fromUriString(UrlUtils.buildFullRequestUrl(request))
                        .replacePath(request.getContextPath())
                        .replaceQuery(null)
                        .fragment(null)
                        .path(PathConstants.OTT_LOGIN_PATH)
                        .queryParam("token", oneTimeToken.getTokenValue());

        String magiclink = builder.toUriString();

        System.out.println("Magic link: " + magiclink);

        // Send magic link via mail
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("The email get from application.yml <"+sender+">");
            message.setTo(oneTimeToken.getUsername());// ...
            message.setSubject("One Time Token");

            String messageBody = """
                    
                    Hello from Spring Security OTT.
                    Use the following link to sign in into the application.
                    
                    %s
                    
                    """.formatted(magiclink);

            message.setText(messageBody);
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Redirect đến trang ott-sent

        System.out.println("oneTimeToken "+oneTimeToken);
        this.redirectHandler.handle(request, response, oneTimeToken);
    }
}
