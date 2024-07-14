package com.vibecast.recommendservice.service;

import com.vibecast.recommendservice.model.UserInteraction;
import com.vibecast.recommendservice.repository.UserInteractionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CsvExportService {
    private final UserInteractionRepo userInteractionRepo;

    public void exportToCsv(String filePath) throws IOException {
        List<UserInteraction> interactions = userInteractionRepo.findAll();

        try (FileWriter writer = new FileWriter(filePath)) {
            for (UserInteraction interaction : interactions) {
                writer.append(String.valueOf(interaction.getId().getUserId()))
                        .append(',')
                        .append(String.valueOf(interaction.getId().getTrackId()))
//                        .append(',')
//                        .append(String.valueOf(aggregatePreference))
                        .append('\n');
            }
        }
    }
}