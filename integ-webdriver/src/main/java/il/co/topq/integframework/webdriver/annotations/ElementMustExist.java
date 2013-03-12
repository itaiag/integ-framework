package il.co.topq.integframework.webdriver.annotations;

import il.co.topq.integframework.webdriver.AbstractAjaxPageObjectImpl;
import il.co.topq.integframework.webdriver.AbstractPageObject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

/**
 * Used on {@link WebElement}s or {@link List}<{@link WebElement}> fields which
 * must exist as the page loads<br>
 * After constructing a new page object from a subclass of
 * {@link AbstractAjaxPageObjectImpl} an annotated element-field will be in the
 * page.<br>
 * The {@link AbstractPageObject#assertInModule()} will try to find the element
 * or a {@link TimeoutException} will be thrown.
 * 
 * @author Aharon hacmon
 * @since 1.0.7
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ElementMustExist {

}