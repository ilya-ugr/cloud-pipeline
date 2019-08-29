package com.epam.pipeline.tesadapter.service;

import com.epam.pipeline.tesadapter.entity.TesCancelTaskResponse;
import com.epam.pipeline.tesadapter.entity.TesListTasksResponse;
import com.epam.pipeline.tesadapter.entity.TesTask;
import com.epam.pipeline.tesadapter.entity.TesCreateTaskResponse;

public interface TesTaskService {

    TesCreateTaskResponse submitTesTask(TesTask body);

    TesListTasksResponse listTesTask();

    void stub();

    TesCancelTaskResponse cancelTesTask(String id);
}
