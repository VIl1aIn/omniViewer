/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eventViewer.errorhandle;

import java.sql.SQLException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author VasilyevIG
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NullPointerException.class)
    public ModelAndView handleNullPointerException(Exception e) {
        ModelAndView mav = new ModelAndView("redirect:/index", HttpStatus.NO_CONTENT);
        String message =  e.getMessage() != null ? e.getMessage():"NullPointerException";
        mav.addObject("errorMsg", "Cause: " + message);
        return mav;
    }

    @ExceptionHandler(SQLException.class)
    public ModelAndView handleSQLException(Exception e) {
    	ModelAndView mav;
    	if (e.getCause().toString().matches(".*Login failed.*")) {
    		mav = new ModelAndView("/index", HttpStatus.UNAUTHORIZED);
    		mav.addObject("errmsg", "Not JDBC connection. Login failed.");
    	} else if (e.getCause().toString().matches(".*Connection refused.*")) {
    		mav = new ModelAndView("/index", HttpStatus.SERVICE_UNAVAILABLE);
    		mav.addObject("errmsg", "Not JDBC connection. Connection refused.");
    	}
    	else {
    		mav = new ModelAndView("/errorspage", HttpStatus.BAD_REQUEST);
    		mav.addObject("errorMsg", e.getCause());
    	}
        return mav;
    }
}
