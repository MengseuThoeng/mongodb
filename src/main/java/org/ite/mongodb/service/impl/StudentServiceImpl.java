package org.ite.mongodb.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ite.mongodb.dto.StudentCreateRequest;
import org.ite.mongodb.dto.StudentResponse;
import org.ite.mongodb.model.Student;
import org.ite.mongodb.repository.StudentRepository;
import org.ite.mongodb.service.StudentService;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    private final MongoTemplate mongoTemplate;

    @Override
    public Page<Student> searchStudents(Map<String, String> filters, String logicalOperator, String sortColumn, String sortDirection, int page, int size) {
        List<Criteria> criteriaList = new ArrayList<>();

        // Building Criteria
        for (Map.Entry<String, String> entry : filters.entrySet()) {
            criteriaList.add(Criteria.where(entry.getKey()).is(entry.getValue()));
        }

        // Combine criteria with logical operator
        Criteria finalCriteria;
        if ("or".equalsIgnoreCase(logicalOperator)) {
            finalCriteria = new Criteria().orOperator(criteriaList.toArray(new Criteria[0]));
        } else {
            finalCriteria = new Criteria().andOperator(criteriaList.toArray(new Criteria[0]));
        }

        Query query = new Query(finalCriteria);

        // Apply sorting
        if (sortColumn != null && sortDirection != null) {
            Sort.Direction direction = Sort.Direction.fromString(sortDirection);
            query.with(Sort.by(direction, sortColumn));
        }

        // Apply pagination
        Pageable pageable = PageRequest.of(page, size);
        long count = mongoTemplate.count(query, Student.class);
        List<Student> students = mongoTemplate.find(query.with(pageable), Student.class);

        // Return a Page object
        return new org.springframework.data.domain.PageImpl<>(students, pageable, count);
    }



    @Override
    public void addStudent(StudentCreateRequest studentCreateRequest) {
        Student student = new Student();
        student.setId(studentCreateRequest.id());
        student.setName(studentCreateRequest.name());
        student.setAddress(studentCreateRequest.address());

        log.info("Student added: {}", student);
        studentRepository.save(student);
    }

    @Override
    public Student getStudentByRno(int rno) {
        return studentRepository.findById(rno).orElseThrow(null);
    }

    @Override
    public Page<Student> getStudents(int page, int size) {
        return studentRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    public Page<StudentResponse> getStudentsResponse(int page, int size, String sortBy, String sortOrder) {

        Sort sort = sortOrder.equalsIgnoreCase(
                Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return studentRepository.findAll(pageable)
                .map(student -> new StudentResponse(
                        student.getId(),
                        student.getName(),
                        student.getAddress()
                ));
    }
}
