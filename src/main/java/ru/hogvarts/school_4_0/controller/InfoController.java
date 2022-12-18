package ru.hogvarts.school_4_0.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hogvarts.school_4_0.service.InfoService;

import java.util.stream.Stream;

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

    @GetMapping("/sum")
    public Long sum() {
        Long time = System.currentTimeMillis();

        Stream.iterate(1, a -> a +1).limit(1_000_000)
                .parallel()
                .reduce(0, Integer::sum);
        time = System.currentTimeMillis() - time;
        return time;
    }


}
