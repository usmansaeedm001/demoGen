package com.digitify.ob.enums;

import com.digitify.framework.exception.ApplicationUncheckException;
import com.digitify.framework.face.EnumerationFace;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.List;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

/**
 * @author Usman
 * @created 8/29/2022 - 10:58 AM
 * @project poc
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum EntityType implements EnumerationFace {
	NOOP("000", "No operation"),
	PARENT("0001", "Parent"),
	CHILD1("0002", "Child1"),
	CHILD2("0003", "Child2"),
	CUSTOMER("0004", "Customer"),
	ACCOUNT("0005", "Account"),
	BANK("0006", "Bank"),
	APPLICATIONCUSTOMER("007", "Application Customer"),
	BANKACCOUNT("008", "Bank Account");


	String code;
	String value;

	EntityType(String code, String value) {
		this.code = code;
		this.value = value;
	}

	public String getCode() {
		return code;
	}

	public String getValue() {
		return value;
	}

	public static EntityType getType(String code) {
		return stream(values()).filter(type -> type.getCode().equalsIgnoreCase(code)).findFirst().orElse(null);
	}

	public static boolean isValid(String code) {
		return stream(values()).anyMatch(type -> type.getCode().equalsIgnoreCase(code));
	}

	public static List<EntityType> getAllTypes() {
		return stream(values()).collect(toList());
	}

	@Override
	public String toString() {
		return code;
	}
}
