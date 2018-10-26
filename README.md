# mySpring
简易实现spring框架的IOC功能
项目工作流程：
在web.xml中
```
<servlet>
    <servlet-name>spring</servlet-name>
    <servlet-class>spring.servlet.MyDispatcherServlet</servlet-class>
    <load-on-startup>1</load-on-startup>
  </servlet>
  <servlet-mapping>
    <servlet-name>spring</servlet-name>
    <url-pattern>/*</url-pattern>
  </servlet-mapping>
```

`MyDispatchServlet`在实例化的时候会初始化`InitBean`,`InitBean`在构造函数中初始化加载`application.xml`里面的属性，实例化其中的`bean`属性，根据`component-scan`属性
可以获得需要扫描的包名，然后扫描对应的包，将其中带有`MyComponent`，`MyController`，`MyRepository`，`MyService`注解的类实例化，注入`beanContainerMap`中，key为
类名和接口，value为对应的对象，然后扫描整个项目，找到带有`MyAutowired`注解的`Field`，注入对应的对象。扫描到的Controller，将其添加到`handlerMapping`中
请求从`web.xml`进入，打在`MyDispatchServlet`，通过`doGet`和`doPost`进行跳转，跳转到`doDispatch`方法上，从`request`中获取参数，注入方法的参数中，执行方法
如果得到`MyModelAndView`就先从中取出Attribute，通过`request.setAttribute(key,value)`加到request中，然后从`MyModelAndView`中取得view，通过`request.getRequestDispatcher(resultAddress).forward(request,response)`
进行页面跳转，流程结束。
