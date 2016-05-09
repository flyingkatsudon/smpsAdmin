package com.humane.util.jasperreports;

import org.springframework.web.servlet.ModelAndView;

import java.util.List;

public class JasperReportsModelAndView extends ModelAndView {
    public JasperReportsModelAndView(String viewName, String format, List<?> datasource) {
        setViewName(viewName);
        addObject("format", format);
        addObject("datasource", datasource);
    }
}
