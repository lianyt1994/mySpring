package spring.springmvc;

import java.util.Map;

public interface MyModel {

    Map<String,Object> addAttribute(String attributeName, Object attributeValue);
    
}