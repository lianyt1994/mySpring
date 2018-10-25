package spring.xml;

import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import spring.constants.Constants;
import spring.enums.XmlRuleEnums;
import spring.exception.XmlException;
import spring.factory.ChildBeanDefinition;
import spring.factory.GenericBeanDefinition;
import spring.utils.ScanUtil;
import spring.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class XmlApplicationContext {

	/**
	 * 将xml中的bean元素注入到容器中的方法
	 * @param contextConfigLocation
	 * @return 返回值是指定xml中的bean的容器
	 */
	public Map<String,GenericBeanDefinition> getBeanDefinitionMap(String contextConfigLocation){
		Map<String,GenericBeanDefinition> beanDefinitionXmlMap = new ConcurrentHashMap<String,GenericBeanDefinition>(256);
		List<Element> elementsList = getElements(contextConfigLocation);
		for (Element element : elementsList) {
			if(element.getName().equals("bean")){
				//new一个bean的容器来盛放bean子元素的容器
				GenericBeanDefinition genericBeanDefinition = new GenericBeanDefinition();
				List<ChildBeanDefinition> childBeanDefinitionList = new ArrayList<ChildBeanDefinition>();

				String beanId = element.attributeValue(XmlRuleEnums.BEAN_RULE.getName());
				String beanClassName = element.attributeValue(XmlRuleEnums.BEAN_RULE.getValue());

				if (StringUtils.isEmpty(beanId) && StringUtils.isEmpty(beanClassName)){
					//设置当前bean的className
					genericBeanDefinition.setClassName(beanClassName);
					List<Element> elements = element.elements();
					if (elements != null){
						for (Element childrenElement : elements) {
							//如果匹配set注入规则，则注入到容器
							if (childrenElement.getName().equals(XmlRuleEnums.SET_INJECT.getType())){
								ChildBeanDefinition childBeanDefinition = new ChildBeanDefinition();
								childBeanDefinition.setChildrenType(XmlRuleEnums.SET_INJECT.getType());
								String name = XmlRuleEnums.SET_INJECT.getName();
								String value = XmlRuleEnums.SET_INJECT.getValue();
								setChildBeanDefinitionByType(childrenElement,childBeanDefinition,name,value,childBeanDefinitionList);
							}
							//如果匹配构造器注入规则，则注入到容器
							if (childrenElement.getName().equals(XmlRuleEnums.CONS_INJECT.getType())){
								ChildBeanDefinition childBeanDefinition = new ChildBeanDefinition();
								childBeanDefinition.setChildrenType(XmlRuleEnums.CONS_INJECT.getType());
								String name = XmlRuleEnums.CONS_INJECT.getName();
								String value = XmlRuleEnums.CONS_INJECT.getValue();
								setChildBeanDefinitionByType(childrenElement,childBeanDefinition,name,value,childBeanDefinitionList);
							}
						}
					}else{
						log.info("{}下面没有子元素",beanId);
					}

					genericBeanDefinition.setChildBeanDefinitionList(childBeanDefinitionList);
					beanDefinitionXmlMap.put(beanId,genericBeanDefinition);
				}
			}
		}
		return null;
	}

	/**
	 * @Description 根据xml指定的component-scan包地址，获得注解扫描的bean容器
	 * @param contextConfigLocation
	 * @return
	 */
	public List<String> getComponentList(String contextConfigLocation){
		List<String> componentList = new ArrayList<String>();
		List<Element> elementsList = getElements(contextConfigLocation);
		for (Element element :
				elementsList) {
			if (element.getName().equals(XmlRuleEnums.SCAN_RULE.getType())) {
				String packageName = element.attributeValue(XmlRuleEnums.SCAN_RULE.getName());
				componentList.addAll(resolveComponentList(packageName));
			}
		}
		//找到所有spring注解的组件
		return componentList;
	}

	/**
	 * 根据要扫描的包名，返回有注解扫描的类
	 * @param packageName
	 * @return
	 */
	public List<String> resolveComponentList(String packageName){

		if (StringUtils.isEmpty(packageName)){
			throw new XmlException("请正确设置"+XmlRuleEnums.SCAN_RULE.getType()+"的属性");
		}
		List<String> componentList = new ArrayList<String>();
		List<String> componentListAfter = ScanUtil.getComponentList(packageName);
		componentList.addAll(componentListAfter);
		return  componentList;
	}

	/**
	 * 将bean的子元素注入容器
	 * @param childrenElement
	 * @param childBeanDefinition
	 * @param name
	 * @param value
	 * @param childBeanDefinitionList
	 */
	private void setChildBeanDefinitionByType(Element childrenElement, ChildBeanDefinition childBeanDefinition, String name, String value, List<ChildBeanDefinition> childBeanDefinitionList) {
		if (childBeanDefinition != null){
			childBeanDefinition.setFirstAttribute(childrenElement.attributeValue(name));
			childBeanDefinition.setSecondAttribute(childrenElement.attributeValue(value));
			childBeanDefinitionList.add(childBeanDefinition);
		}else{
			throw new XmlException("未按规定格式设置");
		}
	}

	/**
	 * 获取xml里面的所有元素
	 * @param contextConfigLocation
	 * @return
	 */
	private List<Element> getElements(String contextConfigLocation) {
		// 创建saxReader对象
		SAXReader reader = new SAXReader();
		// 通过read方法读取一个文件 转换成Document对象
		Document document = null;
		String pathName = Constants.PATH + contextConfigLocation;

		try {
			document= reader.read(pathName);
		} catch (DocumentException e) {
			log.error("没有找到文件,{}",pathName);
		}
		//先获取根元素
		Element rootElement = document.getRootElement();
		//获取所有的bean
		List<Element> elementList = rootElement.elements();
		return elementList;

	}


}
