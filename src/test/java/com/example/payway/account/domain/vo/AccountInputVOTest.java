package com.example.payway.account.domain.vo;

import com.example.payway.support.base.BaseUnitTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountInputVOTest extends BaseUnitTest {

	@Test
	void shouldReturnAccountInputVO() {
			AccountInputVO accountInputVO = new AccountInputVO("12345678901");
			assertEquals("12345678901", accountInputVO.documentNumber());
	}
}