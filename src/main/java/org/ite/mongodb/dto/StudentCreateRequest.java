package org.ite.mongodb.dto;

public record StudentCreateRequest(

        Integer id,

        String name,

        String address,

        String email
) {
}
