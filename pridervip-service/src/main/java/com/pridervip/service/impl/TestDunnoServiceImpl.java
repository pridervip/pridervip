package com.pridervip.service.impl;

import com.pridervip.service.TestDubbo;

class TestDubboServiceImpl implements TestDubbo{

    @Override
    public String sayHi(String name) {
        return "helloword" + name;
    }
}
