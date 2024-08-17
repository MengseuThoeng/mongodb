package org.ite.mongodb.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ite.mongodb.dto.StudentCreateRequest;
import org.ite.mongodb.dto.StudentResponse;
import org.ite.mongodb.model.Student;
import org.ite.mongodb.model.Teacher;
import org.ite.mongodb.repository.StudentRepository;
import org.ite.mongodb.service.StudentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    private final MongoTemplate mongoTemplate;

    public List<Student> search(String search) {
        Query query = new Query();
        Criteria criteria = new Criteria();

        try {
            int rno = Integer.parseInt(search);
            Criteria idCriteria = Criteria.where("rno").is(rno);
            criteria.orOperator(idCriteria);
        } catch (NumberFormatException e) {
            Criteria nameCriteria = Criteria.where("name").regex(search, "i");
            criteria.orOperator(nameCriteria);
        }

        query.addCriteria(criteria);
        return mongoTemplate.find(query, Student.class);
    }

    @Override
    public void addTeacher(Teacher teacher) {
        log.info("Teacher added: {}", teacher);
        mongoTemplate.save(teacher);
    }

    @Override
    public void addStudent(StudentCreateRequest studentCreateRequest) {
        Student student = new Student();
        student.setRno(studentCreateRequest.rno());
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
                        student.getRno(),
                        student.getName(),
                        student.getAddress()
                ));
    }
}
