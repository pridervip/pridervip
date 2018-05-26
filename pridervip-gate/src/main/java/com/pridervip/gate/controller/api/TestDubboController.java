package com.pridervip.gate.controller.api;

import com.pridervip.service.TestDubbo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class TestDubboController {
    /**
     *
     */
    @Resource
    private TestDubbo testDubbo;

    @RequestMapping("/helloword.html")
    @ResponseBody
    public String sayHi(String name) {
        String welcome = testDubbo.sayHi(name);
        return welcome;
  }
}
