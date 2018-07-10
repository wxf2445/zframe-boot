package com.zlzkj.app.mapper;


import com.zlzkj.app.model.index.Authority;
import com.zlzkj.app.model.index.Role;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

public interface PermissionMapper {

    int deleteByRoleId(String roleId);

    List<Authority> selectAuthorityByRoleId(int roleId);

    List<String> selectByRoleId(int roleId);

    List<Role> selectRolesByRoleId(int roleId);
}


//~ Formatted by Jindent --- http://www.jindent.com
