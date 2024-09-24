package com.example.excel.excel.service;

import com.example.excel.excel.entity.ExcelDataEntity;
import com.example.excel.excel.repository.ExcelDataRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportService.class);

    private final ExcelDataRepository excelDataRepository;

    @Transactional(readOnly = true)
    public void generateExcelReport(HttpServletResponse response) throws IOException {
        long start = System.currentTimeMillis();

        SXSSFWorkbook workbook = new SXSSFWorkbook();
        try {
            Sheet sheet = workbook.createSheet("Report");

            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("ID");
            header.createCell(1).setCellValue("Name");
            header.createCell(2).setCellValue("Email");

            response.setHeader("Content-Disposition", "attachment; filename=report.xlsx");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

            int pageSize = 1000; // Number of records to fetch at a time
            int pageNumber = 0;

            List<ExcelDataEntity> dataChunk;

            do {
                // Fetch records in chunks
                dataChunk = excelDataRepository.findAllBy(PageRequest.of(pageNumber, pageSize));

                for (ExcelDataEntity entity : dataChunk) {
                    Row row = sheet.createRow(sheet.getPhysicalNumberOfRows());
                    row.createCell(0).setCellValue(entity.getId());
                    row.createCell(1).setCellValue(entity.getName());
                    row.createCell(2).setCellValue(entity.getEmail());
                }

                // Periodically flush rows to free memory
                if (dataChunk.size() == pageSize) {
                    try {
                        LOGGER.info("Flushing after fetching {} records", (pageSize * pageNumber));
                        ((SXSSFSheet) sheet).flushRows(1000); // Clear memory for flushed rows
                    } catch (IOException e) {
                        throw new RuntimeException("Error flushing rows", e);
                    }
                }

                pageNumber++; // Move to the next chunk
            } while (dataChunk.size() == pageSize); // Continue until fewer than 1000 records are returned

            // Stream the Excel file back to the client
            workbook.write(response.getOutputStream());
            workbook.dispose(); // Dispose of temporary files to free up resources

            long end = System.currentTimeMillis();
            LOGGER.info("It took {} seconds to create the report", ((end - start) / 1000.0));
        } finally {
            LOGGER.info("Closing the workbook...");
            workbook.close();
        }
    }
}

