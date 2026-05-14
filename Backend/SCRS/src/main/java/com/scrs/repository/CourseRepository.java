

package com.scrs.repository;

import com.scrs.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface CourseRepository extends JpaRepository<Course, String> {


    List<Course> findByCourseNameContainingIgnoreCaseOrCourseIdContainingIgnoreCaseOrDepartmentIdContainingIgnoreCase(
            String name, String id, String dept); 


    @Modifying
    @Query(value = "UPDATE courses SET type = ?2 WHERE course_id = ?1", nativeQuery = true)
    void updateType(String courseId, String newType);
}
