package com.cmap;

import java.util.List;

public class DatatableResponse {

	public Long recordsTotal;
	public List<? extends Object> data;
	public Long recordsFiltered;
	
	public DatatableResponse(Long recordsTotal, List<? extends Object> data, Long recordsFiltered) {
		super();
		
		this.recordsTotal = recordsTotal;
		this.data = data;
		this.recordsFiltered = recordsFiltered;
	}
}
