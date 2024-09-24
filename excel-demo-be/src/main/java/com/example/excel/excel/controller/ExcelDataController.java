package com.example.excel.excel.controller;

import com.example.excel.excel.service.ReportService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class ExcelDataController {

    private final ReportService reportService;

    @GetMapping("/download-report")
    public void downloadReport(HttpServletResponse response) throws IOException {
        reportService.generateExcelReport(response);
    }
}
