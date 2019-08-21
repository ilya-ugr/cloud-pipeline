package com.epam.pipeline.tesadapter.service;

import com.epam.pipeline.tesadapter.entity.TesListTasksResponse;
import com.epam.pipeline.tesadapter.entity.TesServiceInfo;

public interface TesTaskService {

    TesListTasksResponse listTesTask();
    TesServiceInfo getServiceInfo();
    void stub();
}
