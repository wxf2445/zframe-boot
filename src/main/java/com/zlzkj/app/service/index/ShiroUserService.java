

package com.zlzkj.app.service.index;

import com.zlzkj.app.model.index.Authority;
import com.zlzkj.app.model.index.User;
import org.apache.shiro.authc.AuthenticationToken;

import java.util.List;

/**
 * Interface description
 *
 *
 * @version        1.0.0, 16/03/31
 * @author         zhm
 */
public interface ShiroUserService {
    User getAuthenticatedUser(AuthenticationToken authcToken);

    User getLoginUser();

    void setUserSessionPermission(List<Authority> permissions);

    List<Authority> getUserSessionPermission();

    void logout ();

    /*boolean checkPermission(String code);*/
}


//~ Formatted by Jindent --- http://www.jindent.com
