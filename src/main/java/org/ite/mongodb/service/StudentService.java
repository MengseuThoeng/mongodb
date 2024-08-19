package org.ite.mongodb.service;

import org.ite.mongodb.dto.StudentCreateRequest;
import org.ite.mongodb.dto.StudentResponse;
import org.ite.mongodb.model.Student;
import org.springframework.data.domain.Page;

public interface StudentService {
    Page<Student> search(int page, int size, String filterAnd, String filterOr, String orders);
    void addStudent(StudentCreateRequest studentCreateRequest);
    Student getStudentByRno(int rno);
    Page<Student> getStudents(int page, int size);
    Page<StudentResponse> getStudentsResponse(int page, int size, String sortBy, String sortOrder);
}
