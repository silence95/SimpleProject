package com.SimpleProject.Generic;

public abstract class GenericTest<P> {

    protected abstract void printParam(P param);
    
    protected abstract boolean verifyParam(P param);
    
    public void showParam(P param) {
        if(!verifyParam(param)) {
            System.out.println("参数验证错误");
            return ;
        }
        printParam(param);
    }
    
}
