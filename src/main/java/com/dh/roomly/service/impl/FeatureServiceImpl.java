package com.dh.roomly.service.impl;

import com.dh.roomly.dto.common.MappingDTO;
import com.dh.roomly.dto.impl.FeatureDTOInput;
import com.dh.roomly.dto.impl.FeatureDTOOutput;
import com.dh.roomly.entity.FeatureEntity;
import com.dh.roomly.exception.DuplicateResourceException;
import com.dh.roomly.exception.ResourceNotFoundException;
import com.dh.roomly.repository.IFeatureRepository;
import com.dh.roomly.service.IFeatureService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FeatureServiceImpl implements IFeatureService {

    private final IFeatureRepository featureRepository;

    @Override
    public List<FeatureDTOOutput> getFeatures() {
        List<FeatureEntity> features = featureRepository.findAll();
        return features.stream()
                .map( feature -> (FeatureDTOOutput) MappingDTO.convertToDto(feature, new FeatureDTOOutput())
                )
                .collect(Collectors.toList());
    }

    @Override
    public FeatureDTOOutput getFeature(Short id) {
        FeatureEntity feature = featureRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Feature with id: " + id + " not found"));
        return (FeatureDTOOutput) MappingDTO.convertToDto(feature, new FeatureDTOOutput());
    }

    @Override
    public FeatureDTOOutput saveFeature(FeatureDTOInput feature) {
        if(featureRepository.existsByNameIgnoreCase(feature.getName())){
            throw new DuplicateResourceException("Feature with name: " + feature.getName() + " already exists");
        }
        FeatureEntity featureEntity = (FeatureEntity) MappingDTO.convertToEntity(feature, FeatureEntity.class);
        FeatureEntity savedFeature = featureRepository.save(featureEntity);
        return (FeatureDTOOutput) MappingDTO.convertToDto(savedFeature, new FeatureDTOOutput());
    }

    @Override
    public FeatureDTOOutput updateFeature(Short id, FeatureDTOInput featureDTOInput) {
        FeatureEntity existingFeature = featureRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Feature with id: " + id + " not found"));

        if (!existingFeature.getName().equalsIgnoreCase(featureDTOInput.getName()) && featureRepository.existsByNameIgnoreCase(featureDTOInput.getName())) {
            throw new DuplicateResourceException("El nombre '" + featureDTOInput.getName() + "' ya está en uso. Por favor, elige otro nombre.");
        }

        existingFeature.setName(featureDTOInput.getName());
        existingFeature.setDescription(featureDTOInput.getDescription());
        existingFeature.setIconName(featureDTOInput.getIconName());

        FeatureEntity updatedFeature = featureRepository.save(existingFeature);
        return (FeatureDTOOutput) MappingDTO.convertToDto(updatedFeature, new FeatureDTOOutput());
    }


    @Override
    public void deleteFeature(Short id) {
        if (!featureRepository.existsById(id)) {
            throw new ResourceNotFoundException("Feature with id: " + id + " not found");
        }
        featureRepository.deleteById(id);
    }
}
