package org.ite.mongodb.controller;

import lombok.RequiredArgsConstructor;
import org.ite.mongodb.dto.StudentCreateRequest;
import org.ite.mongodb.dto.StudentResponse;
import org.ite.mongodb.model.Student;
import org.ite.mongodb.service.StudentService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;


    @GetMapping("/search")
    public Page<Student> searchStudents(
            @RequestParam Map<String, String> allParams,
            @RequestParam(defaultValue = "and") String logicalOperator,
            @RequestParam(required = false) String sortColumn,
            @RequestParam(required = false) String sortDirection,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        // Remove sort and pagination params to isolate filters
        Map<String, String> filters = new HashMap<>(allParams);
        filters.remove("logicalOperator");
        filters.remove("sortColumn");
        filters.remove("sortDirection");
        filters.remove("page");
        filters.remove("size");

        return studentService.searchStudents(filters, logicalOperator, sortColumn, sortDirection, page, size);
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
