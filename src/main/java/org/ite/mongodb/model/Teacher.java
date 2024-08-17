package org.ite.mongodb.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "teacher")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Teacher {

    @Id
    private Integer tno;

    private String name;

    private String email;
}
