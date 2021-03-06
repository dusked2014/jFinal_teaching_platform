package com.tp.util;

public class Constant {
	/*
	 * jwt
	 */
	public static final String JWT_ID = "jwt";
	public static final String JWT_SECRET = "luobiyue12389sujesunn789";
    public static final int JWT_TTL = 60*60*1000;  //millisecond
    public static final int JWT_REFRESH_INTERVAL = 55*60*1000;  //millisecond
    public static final long JWT_REFRESH_TTL = 60*60*2*1000L;  //millisecond：多长时间过期,暂定两小时
    
    public static final long TWO_MONTH_TTL = 60*60*24*30*1000L; // 30天（只取60天以内的文件）
    public static final long ONE_YEAR_TTL = 60*60*24*365*1000L; // 1年（365天）
}
