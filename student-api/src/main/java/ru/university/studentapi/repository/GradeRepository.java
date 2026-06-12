package ru.university.studentapi.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.university.studentapi.entity.GradeEntity;
import ru.university.studentapi.entity.StudentEntity;

public interface GradeRepository extends JpaRepository<GradeEntity, UUID> {
    List<GradeEntity> findByStudentOrderBySubjectAsc(StudentEntity student);
    boolean existsByStudent(StudentEntity student);
    void deleteByStudent(StudentEntity student);
}
