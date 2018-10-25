package spring.factory;

import lombok.extern.slf4j.Slf4j;
import spring.annotation.MyAutowired;
import spring.constants.Constants;
import spring.utils.AnnotationUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class InitBean extends BeanDefinition{

	//初始化后的bean容器 key为class名，value为实例化对象
	public Map<String, Object> beanContainerMap = new ConcurrentHashMap<>();

	public void init(){
		//初始化xml配置
		initXmlBeans(Constants.contextConfigLocation);
		initXmlBeans(Constants.springmvcConfigLocation);
		//初始化扫描注解的配置
		initAutowiredBeans(Constants.contextConfigLocation);

	}

	/**
	 * 实例化，注入的过程
	 * @param contextConfigLocation
	 */
	private void initAutowiredBeans(String contextConfigLocation) {
		List<String> componentList = super.getComponentList(contextConfigLocation);
		System.out.println("实例化的顺序" + componentList);
		//扫描到有注解的类，初始化类的名单
		for (String className :
				componentList) {
			//将每一个类初始化
			try {
				initClass(className);
			} catch (ClassNotFoundException e) {
				log.error("{}没有找到", className);
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			}
		}
	}

	private void initClass(String className) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
		Class<?> aClass = Class.forName(className);
		//先判断这个类有没有接口，如果有接口，将接口装配
		Class<?>[] interfaces = aClass.getInterfaces();

		if (interfaces == null || interfaces.length == 0) {
			noInterfaceInit(className, className);
		}
		else {
			for (Class<?> interfaceClass :
					interfaces) {
				boolean flag = isExistInContainer(className);
				//容器中如果有，则直接使用这个对象进行装配
				if (flag) {
					beanContainerMap.put(interfaceClass.getName(), aClass.newInstance());
				} else {
					//如果容器中没有，则先实例化实现类，然后再装配到容器中
					noInterfaceInit(className, interfaceClass.getName());
				}
			}
		}
	}

	/**
	 * 在实例化该类之前先判断该类在容器中是否存在
	 * @param className
	 * @return
	 */
	public boolean isExistInContainer(String className) {
		Set<Map.Entry<String, Object>> entries = beanContainerMap.entrySet();
		if (entries != null) {
			for (Map.Entry<String, Object> map :
					entries) {
				if (map.getKey().equals(className)) {
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}

	/**
	 * 实例化对象，并放到map里面
	 * @param className
	 * @param interfaceName
	 */
	public void noInterfaceInit(String className, String interfaceName) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
		Class<?> aClass = Class.forName(className);
		//bean实例化
		Object object = aClass.newInstance();
		Field[] declaredFields = aClass.getDeclaredFields();
		for (Field field :
				declaredFields) {
			//如果属性上有MyAutowired注解，则先将属性注入进去
			if (!AnnotationUtils.isEmpty(field.getAnnotation(MyAutowired.class))) {
				//System.out.println("发现注解");
				//设置私有属性可见
				field.setAccessible(true);
				//如果有注解，在实例化链表里面搜寻类名
				Set<Map.Entry<String, Object>> entries = beanContainerMap.entrySet();
				for (Map.Entry<String, Object> entry :
						entries) {
					String type = field.getType().getName();
					if (entry.getKey().equals(type)){
						field.set(object, entry.getValue());
					}
				}
			}
		}
		//放到map里面
		beanContainerMap.put(interfaceName, object);
	}

	/**
	 * 初始化bean中的内容
	 * @param contextConfigLocation
	 */
	public void initXmlBeans(String contextConfigLocation) {
		ApplicationContext applicationContext = new ApplicationContext();
		Class<?> clazz = null;
		//从容器中取出bean，用application的getBean方法依次加载bean
		Map<String,GenericBeanDefinition> beanDefinitionMap = super.getBeanDefinitionMap(contextConfigLocation);
		for (Map.Entry<String, GenericBeanDefinition> entry : beanDefinitionMap.entrySet()) {
			String beanId = entry.getKey();
			String className = entry.getValue().getClassName();
			try {
				clazz = Class.forName(className);

			} catch (ClassNotFoundException e) {
				log.error("xml中{}无法实例化", className);
				e.printStackTrace();
			}
			Object cast = clazz.cast(applicationContext.getBean(beanId));
			beanContainerMap.put(className, cast);
		}
	}

}
