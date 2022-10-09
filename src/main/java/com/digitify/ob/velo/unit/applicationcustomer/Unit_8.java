package com.digitify.ob.velo.unit.applicationcustomer;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.List;
import com.digitify.framework.annotation.Matchable;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

/**
* @created $DATE - $TIME
* @project $PROJECT_NAME
* @author ${USER}
*/
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ApplicationCustomerStatus implements Matchable {

STATUS1("STATUS1", "Status1"),
STATUS2("STATUS2", "Status2"),
STATUS3("STATUS3", "Status3"),
;

String code;
String value;

ApplicationCustomerStatus(String code, String value) {
this.code = code;
this.value = value;
}

public String getCode() {
return code;
}

public String getValue() {
return value;
}

public static ApplicationCustomerStatus getType(String code) {
return stream(values()).filter(type -> type.getCode().equalsIgnoreCase(code)).findFirst().orElse(null);
}

public static boolean isValid(String code) {
return stream(values()).anyMatch(type -> type.getCode().equalsIgnoreCase(code));
}

public static List<ApplicationCustomerStatus> getAllTypes() {
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
