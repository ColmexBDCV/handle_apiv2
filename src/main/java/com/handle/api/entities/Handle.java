package com.handle.api.entities;

import net.handle.hdllib.AdminRecord;
import net.handle.hdllib.Common;
import net.handle.hdllib.Encoder;
import net.handle.hdllib.HandleValue;
import net.handle.hdllib.Util;

public class Handle {
	private HandleValue handleVal;
	private HandleValue handleAdmin = new HandleValue();
	private String URL;
	private String name;
	private String adminHandle = "";
	
	public HandleValue getHandleValue() {
		handleVal = new HandleValue(2, Util.encodeString("URL"), Util.encodeString(this.getURL()));
		return handleVal;
	}
	
	public HandleValue getHandleAdmin() {
		handleAdmin = new HandleValue();
		handleAdmin.setIndex(100);
		handleAdmin.setType(Common.ADMIN_TYPE);
		handleAdmin.setData(Encoder.encodeAdminRecord(new AdminRecord(Util.encodeString(this.getAdminHandle()), 100, 
				true, // addHandle
		        true, // deleteHandle
		        true, // addNA
		        true, // deleteNA
		        true, // readValue
		        true, // modifyValue
		        true, // removeValue
		        true, // addValue
		        true, // modifyAdmin
		        true, // removeAdmin
		        true, // addAdmin
		        true // listHandles
		)));
		return handleAdmin;
	}

	public String getURL() {
		return URL;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAdminHandle() {
		return adminHandle;
	}

	public void setAdminHandle(String adminHandle) {
		this.adminHandle = adminHandle;
	}
	
	
	
}
