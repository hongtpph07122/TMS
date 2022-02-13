package com.oauthcentralization.app.tmsoauth2.config.webConfig;

import com.oauthcentralization.app.tmsoauth2.model.response.UsersResponse;
import com.oauthcentralization.app.tmsoauth2.model.response.UsersRoleResponse;
import com.oauthcentralization.app.tmsoauth2.service.UsersRoleService;
import com.oauthcentralization.app.tmsoauth2.service.UsersService;
import com.oauthcentralization.app.tmsoauth2.service.serviceImpl.MyUserDetailsImpl;
import com.oauthcentralization.app.tmsoauth2.utilities.StringUtility;
import com.oauthcentralization.app.tmsoauth2.utils.ObjectUtils;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.oauthcentralization.app.tmsoauth2.service.serviceImpl.UserDetailsServiceImpl.USER_PREFIX;

@SuppressWarnings({"FieldCanBeLocal"})
public class JWTAuthorizationFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JWTAuthorizationFilter.class);
    private static final String HEADER = "Authorization";
    private static final String PREFIX = "Bearer ";
    private final String jwtSecret;

    @Autowired
    private UsersService usersService;

    @Autowired
    private UsersRoleService usersRoleService;

    public JWTAuthorizationFilter(String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    public void destroy() {
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        try {
            if (checkJwtToken(request, response)) {
                Claims claims = validateToken(request);
                if (ObjectUtils.allNotNull(claims.get("authorities"))) {
                    setUpSpringAuthentication(claims, request);
                }
            }

            if (request.getMethod().equals("OPTIONS")) {
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                chain.doFilter(request, response);
            }

        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private Claims validateToken(HttpServletRequest request) {
        String token = request.getHeader(HEADER).replace(PREFIX, "");
        return Jwts.parser().setSigningKey(jwtSecret.getBytes()).parseClaimsJws(token).getBody();
    }

    private void setUpSpringAuthentication(Claims claims, HttpServletRequest request) {

        UsersResponse usersResponse = usersService.findOne(StringUtils.trimAllWhitespace((String) claims.get("username")));
        List<UsersRoleResponse> usersRoleResponses = usersRoleService.snagUsersRoles(usersResponse.getUserId());
        for (UsersRoleResponse usersRoleResponse : usersRoleResponses) {
            if (StringUtils.isEmpty(usersRoleResponse.getRoleSuffix())) {
                usersRoleResponse.setRoleSuffix(USER_PREFIX);
            }

            if (ObjectUtils.allNotNull(usersRoleResponse.getRoleId())) {
                usersResponse.setRolesId(Collections.singletonList(usersRoleResponse.getRoleId()));
            }
        }

        List<String> roles = usersRoleResponses.stream().map(UsersRoleResponse::getRoleSuffix).collect(Collectors.toList());
        UserDetails userDetails = MyUserDetailsImpl.buildUser(usersResponse, roles);

        /*
                List<String> authorities = (List) claims.get("authorities");
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                claims.getSubject(), null,
                authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        */

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }

    private boolean checkJwtToken(HttpServletRequest request, HttpServletResponse response) {
        String authenticationHeader = request.getHeader(HEADER);
        return !StringUtility.isEmpty(authenticationHeader) && authenticationHeader.startsWith(PREFIX);
    }

}
