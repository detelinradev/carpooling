package com.telerik.carpooling.security;

import javax.servlet.http.HttpServletRequest;

public interface AuthenticationService {

    String getUsername(HttpServletRequest req);

}
