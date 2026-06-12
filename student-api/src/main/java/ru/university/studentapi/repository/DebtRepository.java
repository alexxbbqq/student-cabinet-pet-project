package ru.university.studentapi.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.university.studentapi.entity.DebtEntity;
import ru.university.studentapi.entity.StudentEntity;

public interface DebtRepository extends JpaRepository<DebtEntity, UUID> {
    List<DebtEntity> findByStudentOrderBySubjectAsc(StudentEntity student);
    boolean existsByStudent(StudentEntity student);
    void deleteByStudent(StudentEntity student);
}
