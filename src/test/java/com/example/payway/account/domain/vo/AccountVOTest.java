package com.example.payway.account.domain.vo;

import com.example.payway.support.base.BaseUnitTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountVOTest extends BaseUnitTest {

		@Test
		void shouldCreateAccountVO() {
				Long id = 1L;
				String documentNumber = "12345678901";

				AccountVO accountVO = new AccountVO(id, documentNumber);

				assertEquals(1L, accountVO.id());
				assertEquals("12345678901", accountVO.documentNumber());
		}
}