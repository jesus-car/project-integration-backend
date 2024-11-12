package com.dh.roomly.service;

import java.io.File;

public interface IEmailService {

    // destinatario, asunto, texto
    void sendEmail(String toUser, String subject, String text);
}
