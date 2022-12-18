package ru.hogvarts.school_4_0.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class InfoService {

    @Value("${server.port}")
    private String port;

    public String getPort(){
        return "server port = " + port;
    }

}
