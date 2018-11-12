package com.cmap.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cmap.dao.ScriptStepDAO;
import com.cmap.dao.vo.ScriptStepDAOVO;

@Repository
@Transactional
public class ScriptStepActionDAOImpl implements ScriptStepDAO {

	@Override
	public List<ScriptStepDAOVO> findScriptStepByScriptCode(String scriptCode) {
		// TODO 自動產生的方法 Stub
		return null;
	}

}
