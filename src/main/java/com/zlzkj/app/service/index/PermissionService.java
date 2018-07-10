package com.zlzkj.app.service.index;


import com.zlzkj.app.mapper.PermissionMapper;
import com.zlzkj.app.model.index.Authority;
import com.zlzkj.app.model.index.Role;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;



@Service("permissionService")
@Transactional
public class PermissionService {
    @Resource
    private PermissionMapper mapper;
    @Resource
    private ShiroUserService shiroUserService;

    public List<Authority> selectAuthorityByRoleId(int roleId) {

        return mapper.selectAuthorityByRoleId(roleId);
    }

    public List<String> selectByRoleId(int roleId) {

        return mapper.selectByRoleId(roleId);
    }

    public List<Role> selectRolesByRoleId(int roleId) {
        return mapper.selectRolesByRoleId(roleId);
    }

    private boolean isEmpty(List<?> list) {
        if (isNull(list)) {
            return true;
        } else {
            return list.isEmpty();
        }
    }

    private boolean isNull(Object object) {
        return object == null;

    }

    private boolean isNullOrEmpty(List<?> list) {
        if (isNull(list)) {
            return true;
        }

        return isEmpty(list);

    }

    public void updateAccessControl(String roleId, List<String> authorityIds) {
        mapper.deleteByRoleId(roleId);
        /*for(String key:authorityIds){
            Permission permission = new Permission();
            permission.setId(key);
            permission.setRoleId(roleId);
            int i = mapper.updateByPrimaryKey(permission);
            if(i==0){
                mapper.insert(permission);
            }
        }*/
    }

    public void addAccessControl(String roleId, List<String> authorityIds) {
        /*for(String key:authorityIds){
            Permission permission = new Permission();
            permission.setRoleId(roleId);
            permission.setId(key);
            mapper.insert(permission);
        }*/
    }

	public boolean checkPermission(String code) {
        List<Authority> authoritys = mapper.selectAuthorityByRoleId(shiroUserService.getLoginUser().getRoleId());
        for(Authority authority:authoritys){
        	if(authority.getCode().equals(code))
        		return true;
        }
		return false;
	}
}


//~ Formatted by Jindent --- http://www.jindent.com
