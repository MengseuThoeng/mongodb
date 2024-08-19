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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final MongoTemplate mongoTemplate;

    @Override
    public Page<Student> search(int page, int size, String filterAnd, String filterOr, String orders) {

        log.info("Searching students with filterAnd: {}, filterOr: {}, orders: {}", filterAnd, filterOr, orders);

        Query query = new Query();

        // Add AND filters
        if (filterAnd != null && !filterAnd.isEmpty()) {
            List<Criteria> andCriteria = parseFilterCriteria(filterAnd);
            query.addCriteria(new Criteria().andOperator(andCriteria.toArray(new Criteria[0])));
        }

        // Add OR filters
        if (filterOr != null && !filterOr.isEmpty()) {
            List<Criteria> orCriteria = parseFilterCriteria(filterOr);
            query.addCriteria(new Criteria().orOperator(orCriteria.toArray(new Criteria[0])));
        }

        // Add sorting
        if (orders != null && !orders.isEmpty()) {
            Sort sort = parseSortOrders(orders);
            query.with(sort);
        }

        // Apply pagination
        PageRequest pageRequest = PageRequest.of(page, size);
        query.with(pageRequest);

        // Execute query
        List<Student> students = mongoTemplate.find(query, Student.class);

        // Clone query for count operation to avoid conflict
        Query countQuery = Query.of(query).limit(-1).skip(-1);
        long count = mongoTemplate.count(countQuery, Student.class);

        return new PageImpl<>(students, pageRequest, count);
    }


    private List<Criteria> parseFilterCriteria(String filter) {
        List<Criteria> criteriaList = new ArrayList<>();
        String[] conditions = filter.split(",");

        for (String condition : conditions) {
            String[] parts = condition.split("\\|");
            if (parts.length == 3) {
                String field = parts[0];       // Field name, e.g., "name", "address", etc.
                String operator = parts[1];    // Operator, e.g., "eq", "gt", "regex", etc.
                String value = parts[2];       // Value to compare against, e.g., "mengseu", "pp", etc.

                switch (operator.toLowerCase()) {
                    case "eq":  // Equals
                        criteriaList.add(Criteria.where(field).is(value));
                        break;
                    case "ne":  // Not Equals
                        criteriaList.add(Criteria.where(field).ne(value));
                        break;
                    case "gt":  // Greater Than
                        criteriaList.add(Criteria.where(field).gt(value));
                        break;
                    case "lt":  // Less Than
                        criteriaList.add(Criteria.where(field).lt(value));
                        break;
                    case "gte": // Greater Than or Equal To
                        criteriaList.add(Criteria.where(field).gte(value));
                        break;
                    case "lte": // Less Than or Equal To
                        criteriaList.add(Criteria.where(field).lte(value));
                        break;
                    case "in":  // In List (multiple values separated by ";")
                        criteriaList.add(Criteria.where(field).in(value.split(";")));
                        break;
                    case "nin": // Not In List (multiple values separated by ";")
                        criteriaList.add(Criteria.where(field).nin(value.split(";")));
                        break;
                    case "regex": // Regular Expression (case-insensitive)
                        criteriaList.add(Criteria.where(field).regex(value, "i"));
                        break;
                    case "exists": // Field Exists (true/false)
                        criteriaList.add(Criteria.where(field).exists(Boolean.parseBoolean(value)));
                        break;
                    default:
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid operator: " + operator);
                    // Add more operators as needed
                }
            }
        }
        return criteriaList;
    }

    private Sort parseSortOrders(String orders) {
        List<Sort.Order> sortOrders = new ArrayList<>();
        String[] orderConditions = orders.split(",");
        for (String orderCondition : orderConditions) {
            String[] parts = orderCondition.split("\\|");
            if (parts.length == 2) {
                String field = parts[0];
                Sort.Direction direction = "desc".equalsIgnoreCase(parts[1]) ? Sort.Direction.DESC : Sort.Direction.ASC;
                sortOrders.add(new Sort.Order(direction, field));
            }
        }
        return Sort.by(sortOrders);
    }

    @Override
    public void addStudent(StudentCreateRequest studentCreateRequest) {
        Student student = new Student();
        student.setId(studentCreateRequest.id());
        student.setName(studentCreateRequest.name());
        student.setAddress(studentCreateRequest.address());
        student.setEmail(studentCreateRequest.email());

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
