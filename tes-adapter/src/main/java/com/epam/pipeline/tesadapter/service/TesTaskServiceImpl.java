package com.epam.pipeline.tesadapter.service;

import com.epam.pipeline.tesadapter.entity.TesListTasksResponse;
import com.epam.pipeline.tesadapter.entity.TesServiceInfo;
import org.springframework.stereotype.Service;

@Service
public class TesTaskServiceImpl implements TesTaskService {
    @Override
    public TesListTasksResponse listTesTask() {
        TesListTasksResponse tesListTasksResponse = new TesListTasksResponse();
        return new TesListTasksResponse();
    }

    @Override
    public TesServiceInfo getServiceInfo() {
        return new TesServiceInfo();
    }

    @Override
    public void stub() {
        //stubbed method
    }
}
