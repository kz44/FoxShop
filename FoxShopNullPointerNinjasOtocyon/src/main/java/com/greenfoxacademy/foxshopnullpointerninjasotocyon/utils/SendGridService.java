package com.greenfoxacademy.foxshopnullpointerninjasotocyon.utils;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.User;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SendGridService {
    // This is the id of dynamic template which was created in web api of Send Grid.
    // The template is used for nice html format of sent mails.
    // In the template are two variables: `name` and `link`.
    private static final String TEMPLATE_ID = "d-18fb635d86fe4c48bea95781d72c34e0";
    private final PasswordEncoder passwordEncoder;

    /**
     * Sends a verification email to the specified user using the SendGrid API.
     * <p>
     * This method constructs an email using the SendGrid library, populating it with user-specific
     * information such as name and verification link. The email is then sent to the user's email address
     * for verification.
     * The token in the verification link is encoded username, because username must be unique, so the token
     * would be also unique this way.
     *
     * @param user The user for whom the verification email is to be sent.
     * @throws RuntimeException If an exception occurs during the email sending process.
     */
    public void sendVerificationEmail(User user) {
        SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("/mail/send");

        String name = user.getFirstName() != null ? user.getFirstName() : user.getUsername();

        Mail mail = new Mail();
        Personalization personalization = new Personalization();
        personalization.addTo(new Email(user.getEmail()));
        personalization.addDynamicTemplateData("name", name);
        String token = passwordEncoder.encode(user.getUsername());
        String link = "http://localhost:8080/api/auth/verify"
                .concat("?userId=")
                .concat(String.valueOf(user.getId()))
                .concat("&token=").concat(token);
        personalization.addDynamicTemplateData("link", link);
        mail.addPersonalization(personalization);
        mail.setFrom(new Email("foxshop@post.cz", "FoxShop"));
        mail.setSubject(String.format("Thanks for signing up, %s", name));
        mail.setTemplateId(TEMPLATE_ID);

        try {
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
