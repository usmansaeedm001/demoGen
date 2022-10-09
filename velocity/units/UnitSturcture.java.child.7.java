#set($basePackage = ${Base_package})
#set($typeEnumList = ${Type_Enum_List})
package ${PACKAGE_NAME};

import com.fasterxml.jackson.annotation.JsonFormat;
import com.digitify.framework.annotation.Matchable;
import java.util.List;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

#parse("File Header.java")

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ${NAME}Type implements Matchable {
#if (${typeEnumList} && ${typeEnumList} != "")
#set($list = ${typeEnumList})
#foreach($enumStr in $list.split(","))
#set($enum = $enumStr.trim())
#set($newStr = "")
#foreach($splitStr in $enum.split("_"))
#set($splitStr = $splitStr.replaceAll(" ",""))
#set($splitStr = $splitStr.substring(0,1).toUpperCase()+$splitStr.substring(1))
#set($newStr = $newStr+ " " +$splitStr)
#set($newStr = $newStr.trim())
#end
$enum.toUpperCase()("$enum.toUpperCase()", "${newStr}"),
#end
#else
	INSTANCE("INST", "instance"),
#end
;
##Instance("code","value"),
##;

String code;
String value;

${NAME}Type(String code, String value) {
this.code = code;
this.value = value;
}

public String getCode() {
return code;
}

public String getValue() {
return value;
}

public static ${NAME}Type getType(String code) {
return stream(values()).filter(type -> type.getCode().equalsIgnoreCase(code)).findFirst().orElse(null);
}

public static boolean isValid(String code) {
return stream(values()).anyMatch(type -> type.getCode().equalsIgnoreCase(code));
}

public static List<${NAME}Type> getAllTypes() {
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
