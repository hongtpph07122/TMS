package com.tms.api.service;

import com.tms.api.dto.PbxCallingResponse;
import com.tms.api.dto.Request.ObjectRequestDTO;
import com.tms.api.dto.Request.PhoneCallRequestDTO;

public interface PhoneService {

    PbxCallingResponse createPhoneCall(PhoneCallRequestDTO phoneCallRequestDTO, ObjectRequestDTO objectRequestDTO);

    double snagAsDurations(String linkPlayback);
}
