package ru.kenyo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kenyo.entity.FileEntity;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, String> {

}