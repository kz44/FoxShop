package com.greenfoxacademy.foxshopnullpointerninjasotocyon.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.mail.DynamicTemplateData;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.mail.MailContent;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.User;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SendGridService {

    private final PasswordEncoder passwordEncoder;

    public void sendVerificationEmail(User user) {
        SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("/mail/send");
        String name = user.getFirstName() != null ? user.getFirstName() : "our new user";

        Email from = new Email("foxshop@post.cz", "FoxShop");
        String subject = String.format("Thanks for signing up, %s", name);
        Email to = new Email(user.getEmail());
        String token = passwordEncoder.encode(user.getUsername());
        String link = "http://localhost:8080/api/auth/verify"
                .concat("?userId=")
                .concat(String.valueOf(user.getId()))
                .concat("&token=").concat(token);
        DynamicTemplateData dynamicTemplateData = new DynamicTemplateData(name, link);
        MailContent mailContent = new MailContent(dynamicTemplateData);
        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonInString = mapper.writeValueAsString(mailContent);
            Content content = new Content("text/html", String.format("<p>Thanks for signing up, %s. <a href=%s>Click Me</a> </p>", name, link));
            Mail mail = new Mail(from, subject, to, content);
//            mail.setTemplateId("d-18fb635d86fe4c48bea95781d72c34e0");
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
