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
        mav.addObject("errorMsg", "Cause: " + e.getMessage());
        return mav;
    }

    @ExceptionHandler(SQLException.class)
    public ModelAndView handleSQLException(Exception e) {
        ModelAndView mav = new ModelAndView("/errorspage", HttpStatus.BAD_REQUEST);
        mav.addObject("errorMsg", "Get next SQL error: "
                + e.getCause()
                + " Check you request.");
        return mav;
    }
}
