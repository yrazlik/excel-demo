package com.example.excel.excel.repository;

import com.example.excel.excel.entity.ExcelDataEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExcelDataRepository extends JpaRepository<ExcelDataEntity, Long> {

    List<ExcelDataEntity> findAllBy(Pageable pageable);
}
