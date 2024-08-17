package org.ite.mongodb.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ite.mongodb.model.Student;
import org.ite.mongodb.model.Teacher;
import org.ite.mongodb.service.SearchService;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchServiceImpl implements SearchService {

    private final MongoTemplate mongoTemplate;

    @Override
    public List<Object> search(String search) {
        List<Object> results = new ArrayList<>();

        // Search in Student collection
        Query studentQuery = buildSearchQuery(search, "rno", "name", "address");
        List<Student> students = mongoTemplate.find(studentQuery, Student.class);
        results.addAll(students);

        // Search in Teacher collection
        Query teacherQuery = buildSearchQuery(search, "tno", "name", "email");
        List<Teacher> teachers = mongoTemplate.find(teacherQuery, Teacher.class);
        results.addAll(teachers);

        return results;
    }

    private Query buildSearchQuery(String search, String... fields) {
        List<Criteria> criteriaList = new ArrayList<>();

        // Build criteria for each field
        for (String field : fields) {
            if (isInteger(search)) {
                criteriaList.add(Criteria.where(field).is(Integer.parseInt(search)));
            } else {
                criteriaList.add(Criteria.where(field).regex(search, "i"));
            }
        }

        // Combine criteria using OR operator
        Criteria combinedCriteria = new Criteria().orOperator(criteriaList.toArray(new Criteria[0]));
        return new Query().addCriteria(combinedCriteria);
    }

    private boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
