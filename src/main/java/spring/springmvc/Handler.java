package spring.springmvc;


import lombok.extern.slf4j.Slf4j;
import spring.annotation.MyController;
import spring.annotation.MyRequestMapping;
import spring.exception.SpringmvcException;
import spring.utils.AnnotationUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName Handler
 * @Description  遍历bean容器，在有controller注解的类中有requestmapping扫描的方法，则将方法和url和方法绑定
 */
@Slf4j
public class Handler {

	public static Map<String, Method> bindingRequestMapping(Map<String, Object> beanContainerMap){
		Map<String, Method> handlerMapping = new ConcurrentHashMap<>();
		if (beanContainerMap != null){
			Set<Map.Entry<String, Object>> entries = beanContainerMap.entrySet();

			for (Map.Entry<String, Object> entry :
					entries) {
				Class aClass = entry.getValue().getClass();
				Annotation annotation = aClass.getAnnotation(MyController.class);
				Method[] methods = aClass.getMethods();
				if (!AnnotationUtils.isEmpty(annotation) && methods != null){
					for (Method method:
							aClass.getMethods()) {
						MyRequestMapping requestMappingAnnotation = method.getAnnotation(MyRequestMapping.class);
						if (!AnnotationUtils.isEmpty(requestMappingAnnotation)){
							String key = requestMappingAnnotation.value();
							handlerMapping.put(key,method);
						}
					}
				}

			}

		}
		else{
			throw new SpringmvcException("实例化bean异常，没有找到容器");
		}

		return handlerMapping;

	}


	public static void bindingRequestAndModel(MyModelAndView myModelAndView, HttpServletRequest request) {

		Map<String, Object> modelMap = myModelAndView.getModelMap();
		if (!modelMap.isEmpty()){
			Set<Map.Entry<String, Object>> entries1 = modelMap.entrySet();
			for (Map.Entry<String, Object> entryMap : entries1) {
				String key = entryMap.getKey();
				Object value = entryMap.getValue();
				request.setAttribute(key,value);
			}
		}

	}

	//把参数绑定到request中
	public static List<Object> bingdingMethodParamters(Map<String, Method> bindingRequestMapping, HttpServletRequest request) {
		List<Object> resultParameters  = new ArrayList<>();
		Set<Map.Entry<String, Method>> entries = bindingRequestMapping.entrySet();
		for (Map.Entry<String, Method> entry :
				entries) {
			Method method = entry.getValue();
			Parameter[] parameters = method.getParameters();

			for (Parameter parameter :
					parameters) {
				if (!AnnotationUtils.isEmpty(parameter.getAnnotations())){
					Object resultParameter = null;
					try {
						resultParameter = bingdingEachParamter(parameter, request);
					} catch (Exception e) {
						e.printStackTrace();
						throw new SpringmvcException("绑定参数异常");
					}
					resultParameters.add(resultParameter);
				}
			}
		}
		return resultParameters;
	}

	private static Object bingdingEachParamter(Parameter parameter, HttpServletRequest request) {
		return null;
	}
}
