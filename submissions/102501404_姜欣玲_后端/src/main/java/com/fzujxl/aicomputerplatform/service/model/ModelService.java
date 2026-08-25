package com.fzujxl.aicomputerplatform.service.model;

import com.fzujxl.aicomputerplatform.dto.model.*;
import jakarta.validation.Valid;

public interface ModelService {

    AddModelResponse addModel(@Valid AddModelRequest request);

    ChangeModelStatusResponse publishModel(@Valid PublishModelRequest request);

    ChangeModelStatusResponse offlineModel(@Valid OfflineModelRequest request);

    ChangeCostPointsResponse changeCostPoints(@Valid ChangeCostPointsRequest request);

    void deleteModel(@Valid DeleteModelRequest request);
}
