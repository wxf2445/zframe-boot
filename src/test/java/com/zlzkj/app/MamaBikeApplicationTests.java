package com.zlzkj.app;

import com.zlzkj.app.service.index.UserService;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MamaBikeApplicationTests {

	@Autowired
	private UserService testInterFace;

	@Test
	public void contextLoads() {
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("user",testInterFace.findById(1));
			System.out.println(jsonObject.toString());
		}catch (Exception e){
			e.printStackTrace();
		}
	}

}
