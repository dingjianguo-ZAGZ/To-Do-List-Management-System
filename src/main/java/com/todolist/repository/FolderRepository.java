package com.todolist.repository;

import com.todolist.entity.Folder;
import com.todolist.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 文件夹数据访问层
 */
@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {

    List<Folder> findByUser(User user);

    boolean existsByUserAndName(User user, String name);
}
