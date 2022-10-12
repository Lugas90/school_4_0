package ru.hogvarts.school_4_0.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hogvarts.school_4_0.model.Avatar;

import java.util.Optional;

@Repository
public interface AvatarRepository extends JpaRepository <Avatar, Long> {

    Optional<Avatar> findByStudentId (Long studentId);
}
