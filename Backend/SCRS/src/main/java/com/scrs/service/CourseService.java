

package com.scrs.service;

import com.scrs.model.Course;
import com.scrs.repository.CourseRepository;
import com.scrs.repository.EnrollmentRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {


    @Autowired private CourseRepository courseRepository;
    @Autowired private EnrollmentRepository enrollmentRepository;
    @Autowired private EntityManager entityManager;


    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }


    public List<Course> searchCourses(String keyword) {
        return courseRepository.findByCourseNameContainingIgnoreCaseOrCourseIdContainingIgnoreCaseOrDepartmentIdContainingIgnoreCase(
                keyword, keyword, keyword);
    }


    public Optional<Course> getCourseById(String id) {
        return courseRepository.findById(id);
    }


    public Course addCourse(Course course) {

        return courseRepository.save(course);
    }


    @Transactional
    public Course updateCourse(String id, Course updatedCourse) {

        Course existing = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found: " + id));


        existing.setCourseName(updatedCourse.getCourseName());
        existing.setLecturerId(updatedCourse.getLecturerId());
        existing.setDepartmentId(updatedCourse.getDepartmentId());
        existing.setCredits(updatedCourse.getCredits());
        existing.setMaxCapacity(updatedCourse.getMaxCapacity());


        courseRepository.save(existing);

        String newType = updatedCourse.getType();
        if (newType != null && !newType.equals(existing.getType())) {
            courseRepository.updateType(id, newType);
            entityManager.flush();
            entityManager.clear();
        }


        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found after update: " + id));
    }

    @Transactional
    public void deleteCourse(String id) {

        enrollmentRepository.findByCourseId(id).forEach(e -> enrollmentRepository.delete(e));

        
        courseRepository.deleteById(id);
    }
}