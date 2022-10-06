#set($basePackage = ${Base_package})
#set($tableName = ${Table_name})
#set($typeEnumList = ${Type_Enum_List})
#set($statusEnumList = ${Status_Enum_List})
#set($principal = ${Principal})
#set($PARENT = ${Parent})
#set($apiContextPath = ${Api_context_path})
#set($uniqueField = ${Unique_field})
#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME};#end


#parse("File Header.java")

public class TemplateVariablesValues {
private String NAME = "${NAME}";
private String Base_package = "${Base_package}";
private String Table_name = "${Table_name}";
private String Type_Enum_List = "${Type_Enum_List}";
private String Status_Enum_List = "${Status_Enum_List}";
private String Parent = "${Parent}";
private String Api_context_path = "${Api_context_path}";
private String Principal = "${Principal}";
private String UniqueKey = "${uniqueField}";

}
