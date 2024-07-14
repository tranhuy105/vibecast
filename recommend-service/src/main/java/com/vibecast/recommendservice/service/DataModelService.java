package com.vibecast.recommendservice.service;

import lombok.RequiredArgsConstructor;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.model.DataModel;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class DataModelService {
    private final CsvExportService csvExportService;

    public DataModel createDataModel(String csvFilePath) throws IOException {
        csvExportService.exportToCsv(csvFilePath);
        return new FileDataModel(new File(csvFilePath));
    }
}