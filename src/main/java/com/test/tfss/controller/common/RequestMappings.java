package com.test.tfss.controller.common;

public interface RequestMappings {
	
	public static String EMPLOYEES = "employees";
	
	public static String CONTEXT_PATH = "/";
	
	public static String CREATE_EMPLOYEE = "/add";
	
	public static String RETRIVE_EMPLOYEE_BY_ID = "/{employee-id}";
	
	public static String REMOVE_EMPLOYEE_BY_ID = "/{employee-id}";
	
	public static String UPDATE_EMPLOYEE_BY_ID = "/{employee-id}";
	
	public static String RETRIVE_ALL_EMPLOYEES = "/all";
	
	public static String AUTH = "/auth";
	
	public static String GENERATE_JWS_TOKEN = "/token";
	
}
