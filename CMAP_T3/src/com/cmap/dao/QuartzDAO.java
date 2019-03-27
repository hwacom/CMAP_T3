package com.cmap.dao;

import java.util.List;

import com.cmap.dao.vo.QuartzDAOVO;

public interface QuartzDAO {

	public long countQuartzDataByDAOVO(QuartzDAOVO daoVO) throws Exception;
	
	public List<Object[]> findQuartzDataByDAOVO(QuartzDAOVO daoVO) throws Exception;
}
