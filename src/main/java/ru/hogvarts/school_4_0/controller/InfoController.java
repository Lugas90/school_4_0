package ru.hogvarts.school_4_0.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hogvarts.school_4_0.service.InfoService;

@RestController
@RequestMapping("/getPort")
public class InfoController {


    private final InfoService infoService;

    public InfoController(InfoService infoService) {
        this.infoService = infoService;
    }

    @GetMapping
    public ResponseEntity <String> getPort(){
        return ResponseEntity.ok(infoService.getPort());
    }


}
