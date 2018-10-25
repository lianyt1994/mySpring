package spring.springmvc;

import lombok.Data;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
import java.util.Map;

@Data
public class MyModelAndView  implements MyModel{

	private String view;
	private Map<String,Object> modelMap;

	public MyModelAndView(String view) {
		this.view = view;
	}

	@Override
	public Map<String,Object> addAttribute(String attributeName, Object attributeValue) {
		modelMap.put(attributeName,attributeValue);
		return this.modelMap;
	}
}
