package com.digitify.ob.enums;

import com.digitify.framework.face.EnumerationFace;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.List;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

/**
 * @author Usman
 * @created 9/16/2022 - 10:13 PM
 * @project ProjectTemplate
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode implements EnumerationFace {
	INVALID_REQUEST("001", "Invalid request"),
	NOT_FOUND("002", "Not Found"),
	INVALID_UUID("003", "Invalid uuid"),
	UNABLE_TO_SAVE("004", "unable to save, please try again."),
	UNABLE_TO_UPDATE("005", "unable to update, Please try again"),
	UNABLE_TO_PATCH_UPDATE("006", "unable to patch update, Please try again"),
	ALREADY_EXISTS("007","Already exists"),
	NOT_AUTHORIZED("008", "Not authorized to perform action.");
	String code;
	String value;

	ErrorCode(String code, String value) {
		this.code = code;
		this.value = value;
	}

	public String getCode() {
		return code;
	}

	public String getValue() {
		return value;
	}

	public static ErrorCode getType(String code) {
		return stream(values()).filter(type -> type.getCode().equalsIgnoreCase(code)).findFirst().orElse(null);
	}

	public static boolean isValid(String code) {
		return stream(values()).anyMatch(type -> type.getCode().equalsIgnoreCase(code));
	}

	public static List<ErrorCode> getAllTypes() {
		return stream(values()).collect(toList());
	}

	@Override
	public String toString() {
		return code;
	}
}
