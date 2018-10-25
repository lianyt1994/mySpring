package spring.factory;


import lombok.Data;

import java.util.List;

@Data
public class GenericBeanDefinition {

	/**
	 * 用来放class的类名
	 */
	private  String className;

	/**
	 * 用来存放类的属性
	 */
	private List<ChildBeanDefinition> childBeanDefinitionList;


}
