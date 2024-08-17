package org.ite.mongodb.dto;

public record StudentCreateRequest(

        Integer rno,

        String name,

        String address
) {
}
