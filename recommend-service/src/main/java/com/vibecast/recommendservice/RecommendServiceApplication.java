package com.vibecast.recommendservice;

import com.vibecast.recommendservice.model.UserInteraction;
import com.vibecast.recommendservice.model.UserInteractionId;
import com.vibecast.recommendservice.repository.UserInteractionRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.*;

@SpringBootApplication
public class RecommendServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RecommendServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner initDatabase(UserInteractionRepo repository) {
        return args -> {
            List<UserInteraction> userInteractions = new ArrayList<>();
            Random random = new Random();

            for (long userId = 1; userId <= 20; userId++) {
                // Each user listens to a random number of tracks (between 10 and 90)
                int numTracksListened = random.nextInt(81) + 10;

                // Generate unique track IDs for the user
                Set<Long> trackIds = new HashSet<>();
                while (trackIds.size() < numTracksListened) {
                    trackIds.add((long) (random.nextInt(200) + 1));
                }

                // Create UserInteraction records for the user
                for (long trackId : trackIds) {
                    // Assign preference based on the number of times the user has listened to the track
//                    int listenCount = random.nextInt(100) + 5; // 5 to 100 listens
                    int listenCount = 1;
                    userInteractions.add(new UserInteraction(new UserInteractionId(userId, trackId), (double) listenCount, null));
                }
            }

//            userInteractions.add(new UserInteraction(new UserInteractionId(21L, 100L), 32D,null));
//            userInteractions.add(new UserInteraction(new UserInteractionId(21L, 101L), 46D,null));
//            userInteractions.add(new UserInteraction(new UserInteractionId(21L, 102L), 2D,null));
//            userInteractions.add(new UserInteraction(new UserInteractionId(21L, 103L), 1D,null));
//            userInteractions.add(new UserInteraction(new UserInteractionId(21L, 104L), 66D,null));
//            userInteractions.add(new UserInteraction(new UserInteractionId(21L, 105L), 32D,null));
//            userInteractions.add(new UserInteraction(new UserInteractionId(21L, 106L), 12D,null));
//            userInteractions.add(new UserInteraction(new UserInteractionId(21L, 107L), 23D,null));
//            userInteractions.add(new UserInteraction(new UserInteractionId(21L, 108L), 54D,null));
//            userInteractions.add(new UserInteraction(new UserInteractionId(21L, 109L), 12D,null));
//            userInteractions.add(new UserInteraction(new UserInteractionId(21L, 110L), 3D,null));


            repository.saveAll(userInteractions);
        };
    }

}
