package ru.university.studentapi.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.university.studentapi.entity.ScheduleDayEntity;
import ru.university.studentapi.entity.StudentEntity;

public interface ScheduleDayRepository extends JpaRepository<ScheduleDayEntity, UUID> {
    List<ScheduleDayEntity> findByStudentOrderBySortOrderAsc(StudentEntity student);
    boolean existsByStudent(StudentEntity student);
    void deleteByStudent(StudentEntity student);
}
