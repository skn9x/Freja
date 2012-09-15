package freja.test;

import freja.di.Freja;
import freja.di.config.annotation.Component;
import freja.di.config.annotation.Bind;

/**
 * @author SiroKuro
 */
public class RecursiveTest {
	public static void main(String[] args) {
		System.out.println(Freja.get(ABCDComponent.class).getABCDs('A'));
	}
	
	private interface ABCDComponent {
		public String getABCDs(char c);
	}
	
	@Component
	private static class ABCDComponentImpl implements ABCDComponent {
		@Bind
		private ABCDComponent component = null;
		
		@Override
		public String getABCDs(char c) {
			if ('A' <= c && c <= 'Z')
				return c + component.getABCDs((char) (c + 1));
			else
				return "";
		}
	}
}
