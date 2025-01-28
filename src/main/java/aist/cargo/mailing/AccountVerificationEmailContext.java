package aist.cargo.mailing;

import aist.cargo.entity.User;
import org.springframework.web.util.UriComponentsBuilder;

public class AccountVerificationEmailContext extends AbstractEmailContext {

    private String token;

    @Override
    public <T> void init(T context) {
        User user = (User) context;
        put("userName", user.getUsername());
        setTemplateLocation("mailing/email-verification");
        setSubject("Complete Your Registration");
        setFrom("nalymbaide@gmail.com");
        setTo(user.getEmail());
    }
    public void setToken(String token) {
        this.token = token;
        put("token", token);
    }
    public void buildVerificationUrl(final String baseURL, final String token) {
        final String url = UriComponentsBuilder.fromHttpUrl(baseURL)
                .path("/register/verify").queryParam("token", token).toUriString();
        put("verificationURL", url);
    }
}