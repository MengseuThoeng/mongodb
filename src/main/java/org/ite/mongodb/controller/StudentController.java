package org.ite.mongodb.controller;

import lombok.RequiredArgsConstructor;
import org.ite.mongodb.dto.StudentCreateRequest;
import org.ite.mongodb.dto.StudentResponse;
import org.ite.mongodb.model.Student;
import org.ite.mongodb.model.Teacher;
import org.ite.mongodb.service.StudentService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping("/addTeacher")
    void addTeacher(@RequestBody Teacher teacher) {
        studentService.addTeacher(teacher);
    }

    @GetMapping
    List<Student> search(@RequestParam String search) {
        return studentService.search(search);
    }

    @PostMapping("/addStudent")
    void addStudent(@RequestBody StudentCreateRequest studentCreateRequest) {
        studentService.addStudent(studentCreateRequest);
    }

    @GetMapping("/getStudent/{rno}")
    Student getStudentByRno(@PathVariable int rno) {
        return studentService.getStudentByRno(rno);
    }

    @GetMapping("/getStudents")
    Page<Student> getStudents(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size) {
        return studentService.getStudents(page, size);
    }

    @GetMapping("/getStudentsResponse")
    Page<StudentResponse> getStudentsResponse(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size,
                                              @RequestParam(defaultValue = "rno") String sortBy,
                                              @RequestParam(defaultValue = "ASC") String sortOrder) {
        return studentService.getStudentsResponse(page, size, sortBy, sortOrder);
    }

}
