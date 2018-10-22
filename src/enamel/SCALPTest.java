package enamel;

import org.junit.Test;

public class SCALPTest {

	@Test(expected = NullPointerException.class)
	public void main() throws Exception {
		SCALP.main(null);
	}

}
