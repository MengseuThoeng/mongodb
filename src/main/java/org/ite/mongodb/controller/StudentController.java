package org.ite.mongodb.controller;

import lombok.RequiredArgsConstructor;
import org.ite.mongodb.dto.StudentCreateRequest;
import org.ite.mongodb.dto.StudentResponse;
import org.ite.mongodb.model.Student;
import org.ite.mongodb.service.StudentService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public ResponseEntity<Page<Student>> searchStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String filterAnd,
            @RequestParam(required = false) String filterOr,
            @RequestParam(required = false) String orders
    ) {
        Page<Student> result = studentService.search(page, size, filterAnd, filterOr, orders);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/addStudent")
    public void addStudent(@RequestBody StudentCreateRequest studentCreateRequest) {
        studentService.addStudent(studentCreateRequest);
    }

    @GetMapping("/getStudent/{rno}")
    public Student getStudentByRno(@PathVariable int rno) {
        return studentService.getStudentByRno(rno);
    }

    @GetMapping("/getStudents")
    public Page<Student> getStudents(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size) {
        return studentService.getStudents(page, size);
    }

    @GetMapping("/getStudentsResponse")
    public Page<StudentResponse> getStudentsResponse(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size,
                                                     @RequestParam(defaultValue = "rno") String sortBy,
                                                     @RequestParam(defaultValue = "ASC") String sortOrder) {
        return studentService.getStudentsResponse(page, size, sortBy, sortOrder);
    }
}
