package com.cmap.dao;

import java.util.List;

import com.cmap.model.ProtocolSpec;

public interface ProtocolDAO {

	public List<ProtocolSpec> findAllProtocolSpec();
}
