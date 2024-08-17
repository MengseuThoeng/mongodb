package org.ite.mongodb.repository;

import org.ite.mongodb.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StudentRepository extends MongoRepository<Student, Integer> {
}
