package com.cmap.security;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.cmap.Constants;
import com.cmap.Env;
import com.cmap.model.User;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

	/*
	@Autowired
	LoginService loginService;
	@Autowired
	SysLogService sysLogService;
	 */

	@Autowired
	private HttpServletRequest request;

	//登陸驗證時，通過username獲取使用者的所有權限資訊，
	//並返回User放到spring的全域緩存SecurityContextHolder中，以供授權器使用
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		final boolean isAdmin = request.getSession().getAttribute(Constants.ISADMIN) != null
									? (boolean)request.getSession().getAttribute(Constants.ISADMIN) : false;
		final String password = (String)request.getSession().getAttribute(Constants.PASSWORD);
		final String passhash = (String)request.getSession().getAttribute(Constants.PASSHASH);
		final String ipAddr = (String)request.getSession().getAttribute(Constants.IP_ADDR);
		final String role = (String)request.getSession().getAttribute(Constants.USERROLE);
		final Object error = request.getSession().getAttribute(Constants.ERROR);
		final String[] roles = StringUtils.isNotBlank(role) ? role.split(Env.COMM_SEPARATE_SYMBOL) : null;

		if (StringUtils.equals(Env.LOGIN_AUTH_MODE, Constants.LOGIN_AUTH_MODE_PRTG)) {
			if (error != null && error instanceof ConnectTimeoutException) {
				throw new UsernameNotFoundException("與PRTG連線異常");

			} else if (!isAdmin && StringUtils.isBlank(passhash)) {
				throw new UsernameNotFoundException("帳號或密碼輸入錯誤");
			}
		}

		User user = new User();
		user.setUserName(username);
		user.setPassword(password);
		user.setRoles(roles);
		user.setPasshash(passhash);
		user.setIp(ipAddr);

		boolean accountEnabled = true;
		boolean accountNonExpired = true;
		boolean credentialsNonExpired = true;
		boolean accountNonLocked = true;

		SecurityUser securityUser = new SecurityUser(
				user,
				user.getUserName(),
				new BCryptPasswordEncoder().encode(user.getPassword()),
				//				user.getPassword(),
				accountEnabled,
				accountNonExpired,
				credentialsNonExpired,
				accountNonLocked,
				getAuthorities(user.getRoles())
				);
		//		sysLogService.saveSysLog(new SysLog(user, SysLogService.LOGIN));
		return securityUser;
	}

	public ArrayList<GrantedAuthority> getAuthorities(String... roles) {
		ArrayList<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(roles.length);
		for (String role : roles) {
			Assert.isTrue(!role.startsWith("ROLE_"), role + " cannot start with ROLE_ (it is automatically added)");
			authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
		}
		return authorities;
	}
}
