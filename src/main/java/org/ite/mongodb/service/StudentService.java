package org.ite.mongodb.service;

import org.ite.mongodb.dto.StudentCreateRequest;
import org.ite.mongodb.dto.StudentResponse;
import org.ite.mongodb.model.Student;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface StudentService {

    void addStudent(StudentCreateRequest studentCreateRequest);

    Student getStudentByRno(int rno);

    Page<Student> getStudents(int page, int size);

    Page<StudentResponse> getStudentsResponse(int page, int size, String sortBy, String sortOrder);

    Page<Student> searchStudents(Map<String, String> filters, String logicalOperator, String sortColumn, String sortDirection, int page, int size);
}

