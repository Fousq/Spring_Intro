package kz.zhanbolat.spring.unit;

import kz.zhanbolat.spring.controller.util.AttributeWrapperImpl;
import kz.zhanbolat.spring.exception.ModelAttributeNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

public class AttributeWrapperTest {
    private AttributeWrapperImpl attributeWrapper;

    @BeforeEach
    public void setUp() {
        attributeWrapper = new AttributeWrapperImpl();
    }

    @Test
    public void shouldReturnInteger_whenModelHasObjectWithAttributeName() {
        Model model = new ExtendedModelMap();
        model.addAttribute("number", 10);

        Integer result = attributeWrapper.getInteger(model, "number", "error");

        assertEquals(10, result);
    }

    @Test
    public void shouldReturnString_whenModelHasObjectWithAttributeName() {
        Model model = new ExtendedModelMap();
        model.addAttribute("string", "string");

        String result = attributeWrapper.getString(model, "string", "error");

        assertEquals("string", result);
    }

    @Test
    public void shouldThrowsExceptions_givenNullOrEmptyParameters() {
        Model model = new ExtendedModelMap();
        model.addAttribute("empty", "");

        assertAll(() -> {
            assertCatch(() -> attributeWrapper.getString(model, "not-exists", "error"), "error");
            assertCatch(() -> attributeWrapper.getInteger(model, "not-exists", "error"), "error");
            assertCatch(() -> attributeWrapper.getString(model, "empty", "error"), "error");
        });
    }

    public void assertCatch(Supplier<?> supplier, String expectedError) {
        try {
            supplier.get();
            fail();
        } catch (ModelAttributeNotFoundException e) {
            assertEquals(expectedError, e.getMessage());
        }
    }
}
