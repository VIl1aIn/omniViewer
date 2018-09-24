/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eventViewer.errorhandle;

import javax.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author VasilyevIG
 */
@Controller
public class EViewerErrorController implements ErrorController {

    private static final String ERRPATH = "/error";
    /**
     * Control Errors
     * @param httpRequest
     * @return errorPage
     */
    
    @RequestMapping(value = ERRPATH, method = RequestMethod.GET)
    public ModelAndView generateErrorPage(HttpServletRequest httpRequest) {

        ModelAndView errorPage = new ModelAndView("/errorspage");
        String errorMsg = "Http Error";
        int httpErrorCode = getErrorCode(httpRequest);

        switch (httpErrorCode) {
            case 400: {
                errorMsg = "Http Error Code: 400. Bad Request";
                break;
            }
            case 401: {
                errorMsg = "Http Error Code: 401. Unauthorized";
                break;
            }
            case 404: {
                errorMsg = "Http Error Code: 404. Resource not found";
                break;
            }
            case 500: {
                errorMsg = "Http Error Code: 500. Internal Server Error";
                break;
            }
        }
        errorPage.addObject("errorMsg", errorMsg);
        return errorPage;
    }

    private int getErrorCode(HttpServletRequest httpRequest) {
        return (Integer) httpRequest
                .getAttribute("javax.servlet.error.status_code");
    }
    @Override
    public String getErrorPath() {
        return ERRPATH;
    }
}