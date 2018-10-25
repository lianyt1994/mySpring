package spring.constants;

import spring.xml.FileSystemXmlApplicationContext;

public interface Constants {

	String PATH = FileSystemXmlApplicationContext.class.getResource("/").getPath();
	String contextConfigLocation = "application.xml";
	String springmvcConfigLocation = "spring-mvc.xml";
	String mybatisConfigLocation = "MyUserMapper.xml";
}
