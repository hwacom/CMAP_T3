package com.cmap.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cmap.dao.ScriptListDAO;
import com.cmap.dao.vo.ScriptListDAOVO;

@Repository
@Transactional
public class ScriptListUserDAOImpl extends BaseDaoHibernate implements ScriptListDAO {

	@Override
	public List<ScriptListDAOVO> findScriptListByScriptCode(String scriptCode) {
		// TODO Auto-generated method stub
		return null;
	}

}
