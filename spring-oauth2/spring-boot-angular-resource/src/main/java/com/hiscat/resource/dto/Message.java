package com.hiscat.resource.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class Message {
    private String id = UUID.randomUUID().toString();
    private String content;

    public Message(String content) {
        this.content = content;
    }
}
