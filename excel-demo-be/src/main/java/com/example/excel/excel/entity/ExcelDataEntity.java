package com.example.excel.excel.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "excel_data")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExcelDataEntity {

    @Id
    private Long id;
    private String name;
    private String email;
}
