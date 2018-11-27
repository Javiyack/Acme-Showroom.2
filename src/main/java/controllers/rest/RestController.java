package controllers.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
@RequestMapping("/asynchronous")
public class RestController {

	@RequestMapping(value="/msg/welcome")
	public @ResponseBody String ajaxQuery(HttpServletRequest req, HttpServletResponse res) {
		String result = "";
		return result;
	}
}