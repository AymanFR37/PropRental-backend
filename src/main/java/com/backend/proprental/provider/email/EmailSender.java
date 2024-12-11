package com.backend.proprental.provider.email;

import java.io.File;
import java.util.List;

public interface EmailSender {
    void sendEmail(String to, String subject, String content, List<File> attachments);
    void sendEmail(String to, String subject, String content);
}
