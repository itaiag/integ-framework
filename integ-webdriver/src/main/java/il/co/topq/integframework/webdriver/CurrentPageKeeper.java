package il.co.topq.integframework.webdriver;

import org.openqa.selenium.WebDriver;


/**
 * Holds the last page and get it back from the expected type. would be used in
 * the beginning of every WEB-GUI test<br>
 * For example:<br>
 * <code>
 		CurrentPageKeeper pageKeeper;
 		<br>SystemObjectClass so;<br>
 		public void before(){ <br>
 		so = .... <br>
 		pageKeeper = so.getPageKeeper();<br>
 		<b>pageKeeper.registerPageObjectResolver(so);</b><br>
 		}<br>
 		<br>
		public void test1() {<br>
			LoginWizard loginWizard = pageKeeper.getCurrentPageObject(LoginWizard.class);<br>		
			EULAWizardPage eULAWizardPage = loginWizard.pressNext();<br>
			pageKeeper.setCurrentPageObject(eULAWizardPage);<br>
		}
	</code>
 * 
 * 
 * @author Aharon
 */
public class CurrentPageKeeper implements WebDriverContainer {

	WebDriver driver;

	@Override
	public WebDriver getDriver() {
		return driver;
	}

	@Override
	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}

	/**
	 * Implement this interface in your SO object, in order to create
	 * PageObjectResolver<br>
	 * <code>PageObjectResolver</code> uses dynamic type cast and dynamic class
	 * reference conversion<br>
	 * e.g. <code><br>
	 * 	public &lt;T extends AbstractPageObject&gt; T resolvePage(Class&lt;T&gt; pageType){<br>
	 * 		AbstractSOPage abstractSOPage = new AbstractSOPage(driver) ;<br>
	 * <br>
	 * 		if (pageType.isAssignableFrom(SpecificPage.class)) {<br>
	 * 			return pageType.cast(AbstractSOPage.getSpecificPage());<br>
	 * 		}<br>
	 *  	....<br>
	 * }</code><br>
	 * where <code>getSpecificPage()</code> does some actions, such as clicking etc,. 
	 */
	public interface AbstractPageObjectResolver {
		public <T extends AbstractPageObject> T resolvePage(Class<T> pageType);
	}

	private AbstractPageObjectResolver pageObjectResolver;

	public void registerPageObjectResolver(AbstractPageObjectResolver pageObjectResolver) {
		this.pageObjectResolver = pageObjectResolver;
	}

	private AbstractPageObject currentSimplePageObject;

	/**
	 * 
	 * Returns the last visited page in the PageKeeper.
	 * 
	 * @param pageType
	 *            the type of the required page, such as
	 *            {@code WelcomePage.class}
	 * @param <T>
	 * 	      the generic type of the page type
	 * @return the last page, or trying to create a new one by invoking
	 *         <code>PageObjectResolver.resolvePage(Class&lt;T&gt; pageType)</code>
	 * 
	 * @throws NullPointerException
	 *             when both last page and PageObjectResolver are null
	 * @throws RuntimeException
	 *             caused by a <code>ClassCastException</code> when the last
	 *             page object couldn't be dynamically casted from
	 *             {@code pageType}
	 * @since 02/08/2012
	 */
	public <T extends AbstractPageObject> T getCurrentPageObject(Class<T> pageType) {
		if (currentSimplePageObject == null && pageObjectResolver == null) {
			throw new NullPointerException("Unable to get the page: last page not set");
		} else if (pageType.isInstance(currentSimplePageObject)) {
			T resultPage;
			try {
				resultPage = pageType.cast(currentSimplePageObject);
				resultPage.assertInModule();
				return resultPage;
			} catch (ClassCastException t) {
				// Never happens...
				throw new RuntimeException("Unable to get the page, Type mismatch", t);
			}
		} else {
			return pageObjectResolver.resolvePage(pageType);
		}
	}

	/**
	 * update this page keeper with a new page.
	 * 
	 * @param currentSimplePageObject
	 *            the new page to be kept by the page keeper.
	 */
	public void setCurrentPageObject(AbstractPageObject currentSimplePageObject) {
		this.currentSimplePageObject = currentSimplePageObject;
	}
}
