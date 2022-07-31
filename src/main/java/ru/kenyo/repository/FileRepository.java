package ru.kenyo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.kenyo.entity.FileEntity;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, String> {
    @Query(value = "SELECT * FROM FILE ORDER BY filename LIMIT :limit", nativeQuery = true)
    List<FileEntity> findAllWithLimit(@Param("limit") int limit);
}