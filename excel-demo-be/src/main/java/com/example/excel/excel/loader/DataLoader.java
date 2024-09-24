package com.example.excel.excel.loader;

import com.example.excel.excel.entity.ExcelDataEntity;
import com.example.excel.excel.repository.ExcelDataRepository;
import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataLoader.class);

    private final ExcelDataRepository excelDataRepository;

    @Override
    public void run(String... args) {
        LOGGER.info("Checking whether random data needs to be created...");
        if (excelDataRepository.count() <= 1) {
            LOGGER.info("Random data is being created...");

            // Generate one million records
            for (long i = 0; i < 10; i++) {
                addData(i);
            }

            LOGGER.info("Added one million random data...");
        } else {
            LOGGER.info("Data already exists, skipping...");
        }
    }

    @Transactional
    public void addData(long i) {
        Faker faker = new Faker();
        List<ExcelDataEntity> excelDataList = new ArrayList<>();

        for (long j = 1; j <= 100000; j++) {
            ExcelDataEntity data = new ExcelDataEntity();
            data.setId((100000 * i)+j);
            data.setName(faker.name().fullName());
            data.setEmail(faker.internet().emailAddress());
            excelDataList.add(data);

            if (excelDataList.size() == 1000) {
                LOGGER.info("Save index: {}", ((100000 * i)+j));
                excelDataRepository.saveAll(excelDataList);
                excelDataList.clear();
            }
        }

        if (!excelDataList.isEmpty()) {
            excelDataRepository.saveAll(excelDataList);
        }
    }
}

