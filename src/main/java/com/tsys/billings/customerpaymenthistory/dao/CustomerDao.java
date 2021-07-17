package com.tsys.billings.customerpaymenthistory.dao;

import java.sql.SQLException;

import org.springframework.stereotype.Repository;

@Repository
public class CustomerDao {

	/* mocking up the dao implementaion
	 * Only contain one customer have customer Id "101"
	*/
	public Boolean isCustomerExist(String customerId) throws SQLException{
		if(customerId.equalsIgnoreCase("101")) {
			return Boolean.TRUE;
		}else {
			return Boolean.FALSE;
		}
	}
}
