package com.vibecast.recommendservice.controller;


import com.vibecast.recommendservice.service.CachedDataModelService;
import lombok.RequiredArgsConstructor;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericBooleanPrefItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericBooleanPrefUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class RecommenderController {
    private final CachedDataModelService dataModelService;

    @GetMapping("/recommend/{userId}")
    public List<RecommendedItem> getRecommendations(@PathVariable("userId") Long userId,
                                                    @RequestParam("numRecommendations") int numRecommendations) throws IOException, TasteException {
        String csvFilePath = "data.csv";
        DataModel model = dataModelService.getCachedDataModel(csvFilePath);

        ItemSimilarity userSimilarity = new LogLikelihoodSimilarity(model);
//        UserNeighborhood neighborhood = new NearestNUserNeighborhood(5, userSimilarity, model);
//        Recommender recommender = new GenericBooleanPrefUserBasedRecommender(model, neighborhood, userSimilarity);
        Recommender recommender = new GenericBooleanPrefItemBasedRecommender(model, userSimilarity);

        return recommender.recommend(userId, numRecommendations);
    }
}
