package de.dhbw.karlsruhe.controller;

import org.springframework.web.bind.annotation.*;

@CrossOrigin
@org.springframework.web.bind.annotation.RestController
public class RestController {

  @GetMapping("/api/get")
  String getHelloWorld() {
    return "Hello World";
  }

}
