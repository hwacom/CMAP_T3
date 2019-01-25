package com.cmap.plugin.module.vmswitch;

import com.cmap.exception.ServiceLayerException;

public interface VmSwitchService {

	/**
	 * 將指定 VM Name 切換成備援機
	 * @param vmSwitchVO
	 * @return
	 * @throws ServiceLayerException
	 */
	public String powerOff(VmSwitchVO vmSwitchVO) throws ServiceLayerException;

	/**
	 * 將指定 VM Name 從備援復原
	 * @param vmSwitchVO
	 * @return
	 * @throws ServiceLayerException
	 */
	public String powerOn(VmSwitchVO vmSwitchVO) throws ServiceLayerException;
}
