package com.easycarpool.test;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Service(value="demo")
@RequestMapping("/demo")
public class NewTest {

	@RequestMapping(value= "test", method = RequestMethod.POST)
	@ResponseBody
	public String authenticateUser(HttpServletRequest request){
		String userName = request.getParameter("userName");
		String secretPin = request.getParameter("secretPin");
		if(userName!=null){
			userName = userName.toLowerCase();
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("ranjit_behura", "111111");
		map.put("anoop_aswal", "222222");
		map.put("abhay_c", "333333");
		map.put("shrikant_deo", "444444");
		map.put("dikshant", "555555");

		if(userName != null && secretPin == null){
			if(map.containsKey(userName)){
				return "success";
			}else{
				return "failure";
			}
		}
		else if(userName != null && secretPin != null){
			if(map.containsKey(userName)){
				if(secretPin.equals("666666") || secretPin.equals("777777") ||secretPin.equals("000000")){
					return "300";
				}
				if(map.get(userName).equalsIgnoreCase(secretPin)){
					return "100";
				}else {
					return "200";
				}
			}else{
				return "200";
			}
		}
		return "200";
		
	}
}
