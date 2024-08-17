package org.ite.mongodb.service;

import org.ite.mongodb.dto.StudentCreateRequest;
import org.ite.mongodb.dto.StudentResponse;
import org.ite.mongodb.model.Student;
import org.ite.mongodb.model.Teacher;
import org.springframework.data.domain.Page;

import java.util.List;

public interface StudentService {

    void addStudent(StudentCreateRequest studentCreateRequest);

    Student getStudentByRno(int rno);

    Page<Student> getStudents(int page, int size);

    Page<StudentResponse> getStudentsResponse(int page, int size, String sortBy, String sortOrder);

    List<Student> search(String search);

    void addTeacher(Teacher teacher);
}
