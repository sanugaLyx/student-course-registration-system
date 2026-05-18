

package com.scrs.repository;

import com.scrs.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {


    Optional<Student> findByEmailAndPassword(String email, String password);


    boolean existsByEmail(String email);


    @Modifying
    @Query(value = "UPDATE students SET type = ?2 WHERE student_id = ?1", nativeQuery = true)
    void updateType(String studentId, String newType);
}
