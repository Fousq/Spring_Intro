package kz.zhanbolat.spring.controller.util;

import kz.zhanbolat.spring.exception.ModelAttributeNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.Objects;

@Component
public class AttributeWrapperImpl {

    public Integer getInteger(Model model, String attributeName, String errorMsg) {
        Integer attribute;
        try {
            attribute = (Integer) model.getAttribute(attributeName);
        } catch (ClassCastException e) {
            throw new ModelAttributeNotFoundException(errorMsg, e);
        }
        if (Objects.isNull(attribute)) {
            throw new ModelAttributeNotFoundException(errorMsg);
        }
        return attribute;
    }

    public String getString(Model model, String attributeName, String errorMsg) {
        String attribute;
        try {
            attribute = (String) model.getAttribute(attributeName);
        } catch (ClassCastException e) {
            throw new ModelAttributeNotFoundException(errorMsg, e);
        }
        if (Objects.isNull(attribute) || attribute.isEmpty()) {
            throw new ModelAttributeNotFoundException(errorMsg);
        }
        return attribute;
    }
}
