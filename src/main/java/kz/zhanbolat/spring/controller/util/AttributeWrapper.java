package kz.zhanbolat.spring.controller.util;

import org.springframework.ui.Model;

public interface AttributeWrapper {
    Integer getInteger(Model model, String attributeName, String errorMsg);
    String getString(Model model, String attributeName, String errorMsg);
}
