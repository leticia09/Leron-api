package com.leron.api.controller;

import com.leron.api.commons.TypeCardEnum;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/modality")
public class ModalityController {
    @GetMapping("")
    public List<Map<String, Object>> getTypeCardList() {
        List<Map<String, Object>> typeCardList = new ArrayList<>();

        for (TypeCardEnum typeCardEnum : TypeCardEnum.values()) {
            Map<String, Object> typeCardItem = Map.of(
                    "id", typeCardEnum.getKey(),
                    "name", typeCardEnum.getValue()
            );

            typeCardList.add(typeCardItem);
        }

        return typeCardList;
    }
}
