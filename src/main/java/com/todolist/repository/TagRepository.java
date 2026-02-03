package com.todolist.repository;

import com.todolist.entity.Tag;
import com.todolist.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 标签数据访问层
 */
@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    List<Tag> findByUser(User user);

    Optional<Tag> findByUserAndName(User user, String name);

    boolean existsByUserAndName(User user, String name);
}
