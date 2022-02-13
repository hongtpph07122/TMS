package com.tms.api.rest;

import com.tms.api.request.ConfigLogDevRequestDTO;
import com.tms.api.response.ConfigLogDevResponseDTO;
import com.tms.api.response.TMSResponse;
import com.tms.api.service.ConfigLogDevService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/config-dev")
public class ConfigLogDevController {

    private final ConfigLogDevService configDevService;

    @Autowired
    public ConfigLogDevController(ConfigLogDevService configDevService) {
        this.configDevService = configDevService;
    }


    @PostMapping("/new")
    public @ResponseBody
    ResponseEntity<?> createOne(@RequestBody ConfigLogDevRequestDTO configLogDevRequest) {
        return new ResponseEntity<>(TMSResponse.buildResponse(configDevService.createOne(configLogDevRequest)), HttpStatus.OK);
    }

    @PutMapping("/update")
    public @ResponseBody
    ResponseEntity<?> updateOne(@RequestParam("id") Long id, @RequestBody ConfigLogDevResponseDTO configLogDevRequest) {
        if (!configDevService.existById(id)) {
            return new ResponseEntity<>(TMSResponse.buildResponse(null, 0, "id unavailable", 400), HttpStatus.OK);
        }
        return new ResponseEntity<>(TMSResponse.buildResponse(configDevService.updateOne(id, configLogDevRequest)), HttpStatus.OK);
    }

    @GetMapping("/one")
    public @ResponseBody
    ResponseEntity<?> findOne(@RequestParam("id") Long id) {
        if (!configDevService.existById(id)) {
            return new ResponseEntity<>(TMSResponse.buildResponse(null, 0, "id unavailable", 400), HttpStatus.OK);
        }
        return new ResponseEntity<>(TMSResponse.buildResponse(configDevService.findOne(id)), HttpStatus.OK);
    }

    @GetMapping("/all")
    public @ResponseBody
    ResponseEntity<?> findAll() {
        return new ResponseEntity<>(TMSResponse.buildResponse(configDevService.findAll()), HttpStatus.OK);
    }

    @GetMapping("/one-val")
    public @ResponseBody
    ResponseEntity<?> findOne(@RequestParam("value") Integer value) {
        if (!configDevService.existByValue(value)) {
            return new ResponseEntity<>(TMSResponse.buildResponse(null, 0, "value unavailable", 400), HttpStatus.OK);
        }
        return new ResponseEntity<>(TMSResponse.buildResponse(configDevService.findOneByValue(value)), HttpStatus.OK);
    }

    @DeleteMapping("/one")
    public @ResponseBody
    ResponseEntity<?> deleteOne(@RequestParam("id") Long id) {
        if (!configDevService.existById(id)) {
            return new ResponseEntity<>(TMSResponse.buildResponse(null, 0, "id unavailable", 400), HttpStatus.OK);
        }
        return new ResponseEntity<>(TMSResponse.buildResponse(configDevService.deleteById(id)), HttpStatus.OK);
    }
}
