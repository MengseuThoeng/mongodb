package org.ite.mongodb.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "student")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    @Id
    private Integer rno;

    private String name;

    private String address;

    private String role;
}
