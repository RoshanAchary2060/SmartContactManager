package com.roshan.helper;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message {
    private String content;
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private MessageType type = MessageType.blue;
}