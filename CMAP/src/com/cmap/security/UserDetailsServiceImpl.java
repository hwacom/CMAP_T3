package com.cmap.security;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

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
	
	//登陸驗證時，通過username獲取使用者的所有權限資訊，  
    //並返回User放到spring的全域緩存SecurityContextHolder中，以供授權器使用
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = !username.equals("R2user") && !username.equals("admin") && !username.equals("prtgadmin") && !username.equals("R1user") ? null : new User();
		if(null == user) throw new UsernameNotFoundException("User not found.");
		
		if (StringUtils.equals(username, "R2user")) {
			user.setUserName("R2user");
			user.setPassword("R2user123");
			user.setRole("USER");
			
		} else if (StringUtils.equals(username, "admin")) {
			user.setUserName("admin");
			user.setPassword("admin123");
			user.setRole("USER");
			
		} else if (StringUtils.equals(username, "prtgadmin")) {
			user.setUserName("prtgadmin");
			user.setPassword("prtgadmin");
			user.setRole("USER");
			
		} else if (StringUtils.equals(username, "R1user")) {
			user.setUserName("R1user");
			user.setPassword("R1user123");
			user.setRole("USER");
		}
		
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
				getAuthorities(user.getRole())
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
