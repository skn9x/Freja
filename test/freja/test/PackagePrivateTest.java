package freja.test;

import freja.di.Freja;
import freja.di.config.annotation.Component;

public class PackagePrivateTest {
	public static void main(String[] args) {
		System.out.println(Freja.get(SampleComponent.class).helloworld());
	}
	
	private interface SampleComponent {
		public String helloworld();
	}
	
	@Component
	private static class SampleComponentImpl implements SampleComponent {
		@Override
		public String helloworld() {
			return "Hello World";
		}
	}
}
