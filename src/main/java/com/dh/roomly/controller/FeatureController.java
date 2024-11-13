package com.dh.roomly.controller;

import com.dh.roomly.dto.impl.FeatureDTOInput;
import com.dh.roomly.dto.impl.FeatureDTOOutput;
import com.dh.roomly.service.IFeatureService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/features")
@AllArgsConstructor
public class FeatureController {
    private final IFeatureService featureService;

    @GetMapping("{featuredId}")
    public ResponseEntity<FeatureDTOOutput> getFeature(@PathVariable("featuredId") Short featuredId) {
        return ResponseEntity.ok(featureService.getFeature(featuredId));
    }
    @GetMapping("/all")
    public ResponseEntity<List<FeatureDTOOutput>> getAllFeatures() {
        return ResponseEntity.ok(featureService.getFeatures());
    }
    @PostMapping("/new")
    public ResponseEntity<FeatureDTOOutput> createFeature(@Valid @RequestBody FeatureDTOInput featureDTOInput) {
        FeatureDTOOutput createdFeature = featureService.saveFeature(featureDTOInput);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFeature);
    }

    @PutMapping("/update/{featureId}")
    public ResponseEntity<FeatureDTOOutput> updateFeature(
            @PathVariable("featureId") Short featureId,
            @Valid @RequestBody FeatureDTOInput featureDTOInput) {

        FeatureDTOOutput updatedFeature = featureService.updateFeature(featureId, featureDTOInput);
        return ResponseEntity.ok(updatedFeature);
    }

    @DeleteMapping("/delete/{featureId}")
    public ResponseEntity<Void> deleteFeature(@PathVariable("featureId") Short featureId) {
        featureService.deleteFeature(featureId);
        return ResponseEntity.noContent().build();
    }
}
