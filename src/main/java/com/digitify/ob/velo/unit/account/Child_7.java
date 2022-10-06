package com.digitify.ob.velo.unit.account;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.digitify.framework.annotation.Matchable;
import java.util.List;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

/**
* @created $DATE - $TIME
* @project $PROJECT_NAME
* @author ${USER}
*/
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum AccountType implements Matchable {
	INSTANCE("INST", "instance"),
;

String code;
String value;

AccountType(String code, String value) {
this.code = code;
this.value = value;
}

public String getCode() {
return code;
}

public String getValue() {
return value;
}

public static AccountType getType(String code) {
return stream(values()).filter(type -> type.getCode().equalsIgnoreCase(code)).findFirst().orElse(null);
}

public static boolean isValid(String code) {
return stream(values()).anyMatch(type -> type.getCode().equalsIgnoreCase(code));
}

public static List<AccountType> getAllTypes() {
return stream(values()).collect(toList());
}

@Override
public String toMatchAbleString() {
return this.code;
}

@Override
public String toString() {
return code;
}
}
