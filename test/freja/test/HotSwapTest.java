package freja.test;

import freja.di.Freja;
import freja.di.config.annotation.AnnotationConfiguration;
import freja.di.config.annotation.Component;
import freja.di.internal.DefaultContainer;

/**
 * @author SiroKuro
 */
public class HotSwapTest {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Freja.init(new DefaultContainer(true), AnnotationConfiguration.getDefaultSetting());
		SampleComponent component = Freja.get(SampleComponent.class);
		
		try {
			while (true) {
				component.print();
				Thread.sleep(4000);
			}
		}
		catch (InterruptedException ex) {
			;
		}
	}
	
	public interface SampleComponent {
		public void print();
	}
	
	@Component
	public static class SampleComponentImpl implements SampleComponent {
		@Override
		public void print() {
			System.out.println("hello");
		}
	}
}
