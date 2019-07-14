package com.abs.spo.service;

import com.abs.spo.controller.dto.WorkforceRequestDTO;
import com.abs.spo.exception.NoSolutionNotFoundException;
import com.abs.spo.model.WorkforceAssignee;

public interface WorkforceOptimizerService {
    WorkforceAssignee[] customGASolution(WorkforceRequestDTO dto) throws NoSolutionNotFoundException;
}
