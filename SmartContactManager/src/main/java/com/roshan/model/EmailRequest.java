package com.roshan.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class EmailRequest {
    private String recipientEmail;
    private String subject;
    private String message;
}
