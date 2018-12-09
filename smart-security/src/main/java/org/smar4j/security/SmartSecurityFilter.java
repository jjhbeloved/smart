package org.smar4j.security;

import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.CachingSecurityManager;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.servlet.ShiroFilter;
import org.smar4j.security.realm.SmartCustomRealm;

import java.util.Set;

/**
 * @author david
 * @since created by on 18/12/9 20:18
 */
public class SmartSecurityFilter extends ShiroFilter {

	@Override
	public void init() throws Exception {
		super.init();
		WebSecurityManager webSecurityManager = super.getSecurityManager();
		// 设置多个 realm, 用","分割
		setRealms(webSecurityManager);
		// 设置 cache
		setCache(webSecurityManager);
	}


	private void setRealms(WebSecurityManager webSecurityManager) {
		String r = SecurityConfig.getRealms();
		if (StringUtils.isBlank(r)) {
			return;
		}
		String[] realmArray = r.split(",");
		Set<Realm> realms = Sets.newLinkedHashSet();
		for (String realm : realmArray) {
			if (SecurityConstant.REALMS_CUSTOM.equals(realm)) {
				addCustomRealm(realms);
			}
		}
		RealmSecurityManager realmSecurityManager = (RealmSecurityManager) webSecurityManager;
		realmSecurityManager.setRealms(realms);
	}

	private void addJdbcRealm() {

	}


	private void addCustomRealm(Set<Realm> realms) {
		SmartSecurity smartSecurity = SecurityConfig.getSmartSecurity();
		SmartCustomRealm smartCustomRealm = new SmartCustomRealm(smartSecurity);
		realms.add(smartCustomRealm);
	}

	private void setCache(WebSecurityManager webSecurityManager) {
		if (!SecurityConfig.isCache()) {
			return;
		}
		CachingSecurityManager cachingSecurityManager = (CachingSecurityManager) webSecurityManager;
		// 使用基于内存的 管理器
		CacheManager cacheManager = new MemoryConstrainedCacheManager();
		cachingSecurityManager.setCacheManager(cacheManager);
	}

}
