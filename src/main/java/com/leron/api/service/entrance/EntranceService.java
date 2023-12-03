package com.leron.api.service.entrance;

import com.leron.api.model.DTO.entrance.EntranceRequest;
import com.leron.api.model.DTO.entrance.EntranceResponse;
import com.leron.api.responses.DataResponse;
import org.springframework.stereotype.Service;

@Service
public class EntranceService {

    public static DataResponse<EntranceResponse> create( EntranceRequest requestDTO, String locale, String authorization) {
        return null;
    }
}
