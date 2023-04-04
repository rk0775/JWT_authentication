package com.blackcoffer2.api.configuration;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.blackcoffer2.api.jwtHelper.JwtUtil;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{
	
	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private UserCustomConfigService userCustomConfigService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String fullToken = request.getHeader("Authorization");
		String userName=null;
		String token=null;
		
		//token is empty or not and check the token format here...
		if(fullToken!=null && fullToken.startsWith("Bearer ")) {
			//(bearer gdhfgejdhd) so actual token start from 7 index 
			token=fullToken.substring(7);
			try {
				userName=this.jwtUtil.getUsernameFromToken(token);
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			UserDetails userDetails=this.userCustomConfigService.loadUserByUsername(userName);
			//security work
			if(userName!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
				
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
				usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			
			}else {
				System.out.println("token is not valideted..");
			}
			
		}
		filterChain.doFilter(request, response);
	}
	
}
