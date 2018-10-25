package spring.enums;

import lombok.Getter;

@Getter
public enum  XmlRuleEnums {
	//bean规则
	BEAN_RULE("bean","id","class"),

	//扫描规则
	SCAN_RULE("component-scan","base-package","null"),

	//注入规则
	SET_INJECT("property","name","value"),

	CONS_INJECT("constructor-arg", "value", "index"),

	;


	private String type;

	private String name;

	private String value;

	XmlRuleEnums(String type, String name, String value) {
		this.type = type;
		this.name = name;
		this.value = value;
	}
}
