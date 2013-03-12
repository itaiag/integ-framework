package il.co.topq.integframework.webdriver.annotations;

import il.co.topq.integframework.webdriver.frames.ByFramePath;
import il.co.topq.integframework.webdriver.windows.ByWindowTitle;

import java.lang.reflect.Field;

import org.openqa.selenium.By;

/**
 * 
 * @author Aharon Hacmon
 * @since 1.07
 */
public class Annotations extends org.openqa.selenium.support.pagefactory.Annotations {

	private final Field field;
	public Annotations(Field field) {
		super(field);
		this.field = field;
	}

	@Override
	public By buildBy() {
		ElementInFrameList framelist;
		if ((framelist = field.getAnnotation(ElementInFrameList.class)) != null) {
			return new ByFramePath(super.buildBy(), framelist.value());
		} 
		ElementInWindow window;
		if ((window = field.getAnnotation(ElementInWindow.class)) != null){
			return new ByWindowTitle(super.buildBy(),window.windowTitle());
		}
		else {
			return super.buildBy();
		}
	}

}
