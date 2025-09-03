package com.ganesh.LifeStyleMatcherProject;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;

import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.Base64;
import java.util.Properties;

public class GmailService {

    private static final String APPLICATION_NAME = "Spring Gmail API";
    private static final String CREDENTIALS_FILE = "client_secret_334698275353-5ficq1064qm89ao9msnni21foovc4as6.apps.googleusercontent.com.json";

    public static Gmail getService() throws Exception {
        GoogleCredentials credentials = GoogleCredentials
                .fromStream(new FileInputStream(CREDENTIALS_FILE))
                .createScoped("https://www.googleapis.com/auth/gmail.send");

        return new Gmail.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credentials))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public static void sendMail(String to, String subject, String body) throws Exception {
        Gmail service = getService();

        MimeMessage email = createEmail(to, subject, body);
        com.google.api.services.gmail.model.Message message = createMessageWithEmail(email);

        service.users().messages().send("me", message).execute();
    }

    private static MimeMessage createEmail(String to, String subject, String bodyText) throws Exception {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress("me"));
        email.addRecipient(jakarta.mail.Message.RecipientType.TO, new InternetAddress(to));
        email.setSubject(subject);
        email.setText(bodyText);
        return email;
    }

    private static com.google.api.services.gmail.model.Message createMessageWithEmail(MimeMessage email) throws Exception {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        email.writeTo(buffer);
        String encodedEmail = Base64.getUrlEncoder().encodeToString(buffer.toByteArray());
        com.google.api.services.gmail.model.Message message = new com.google.api.services.gmail.model.Message();
        message.setRaw(encodedEmail);
        return message;
    }
}
