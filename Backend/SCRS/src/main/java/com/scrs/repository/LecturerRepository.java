
package com.scrs.repository;

import com.scrs.model.Lecturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface LecturerRepository extends JpaRepository<Lecturer, String> {


    Optional<Lecturer> findByEmailAndPassword(String email, String password);


    boolean existsByEmail(String email);


    @Modifying
    @Query(value = "UPDATE lecturers SET type = ?2 WHERE lecturer_id = ?1", nativeQuery = true)
    void updateType(String lecturerId, String newType);
}