package com.dh.roomly.service;

import com.dh.roomly.dto.impl.FeatureDTOInput;
import com.dh.roomly.dto.impl.FeatureDTOOutput;
import java.util.List;

public interface IFeatureService {
    List<FeatureDTOOutput> getFeatures();
    FeatureDTOOutput getFeature(Short id);
    FeatureDTOOutput saveFeature(FeatureDTOInput feature);
    FeatureDTOOutput updateFeature(Short id, FeatureDTOInput feature);
    void deleteFeature(Short id);
}
