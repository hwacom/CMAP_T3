package com.cmap.utils;

import java.util.List;

import com.cmap.Env;

public interface EnvUtils {

	public void initEnv() throws Exception;
	
	public void refreshAll() throws Exception;
	
	public void refreshByNames(List<String> settingNames) throws Exception;
	
	public List<Env> findEnvs(String settingName) throws Exception;
	
	public Env findEnv(String settingName) throws Exception;
}
