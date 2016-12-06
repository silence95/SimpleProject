package com.generic;

class TestParam {
    
    private String id;
    private String name;

    TestParam() {}
    
    TestParam(String id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

public class GenericTestImpl extends GenericTest<TestParam> {

    @Override
    protected void printParam(TestParam param) { // 这里会出现桥方法  http://blog.csdn.net/lonelyroamer/article/details/7868820
        System.out.println("id: " + param.getId() + ",name: " + param.getName());
    } 

    @Override
    protected boolean verifyParam(TestParam param) {
        if(param.getId() == null || "".equals(param.getId()))
            return false;
        if(param.getName() == null || "".equals(param.getName()))
            return false;
        return true;
    }

    public static void main(String[] args) {
        TestParam param = new TestParam("1","A");
        GenericTest<TestParam> genericTestImpl = new GenericTestImpl();
        genericTestImpl.showParam(param);
        System.out.println(genericTestImpl instanceof GenericTest<?>);
        
    }
   
}
