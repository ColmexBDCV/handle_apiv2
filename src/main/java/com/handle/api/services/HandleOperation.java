package com.handle.api.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.PrivateKey;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.handle.api.entities.Handle;
import com.handle.api.entities.HandleResponse;

import net.handle.apps.batch.BatchUtil;
import net.handle.apps.batch.ListHandlesUtil;
import net.handle.hdllib.AbstractResponse;
import net.handle.hdllib.AuthenticationInfo;
import net.handle.hdllib.CreateHandleRequest;
import net.handle.hdllib.DeleteHandleRequest;
import net.handle.hdllib.HandleException;
import net.handle.hdllib.HandleResolver;
import net.handle.hdllib.HandleValue;
import net.handle.hdllib.ModifyValueRequest;
import net.handle.hdllib.PublicKeyAuthenticationInfo;
import net.handle.hdllib.SiteInfo;
import net.handle.hdllib.Util;

public class HandleOperation {
	
	private static final Logger logger = LogManager.getLogger(HandleOperation.class);
	private String base_url = "http://hdl.handle.net/";
	private String adminHandle = "0.NA/20.500.11986";
	private HandleResolver resolver = new HandleResolver();
	
	public HandleResponse createHandle(Handle handle) throws HandleException {
		handle.setAdminHandle(adminHandle);
		resolver.traceMessages = true;
		AuthenticationInfo auth = this.authenticate(adminHandle); 
		if(auth != null) {
			HandleValue[] values = new HandleValue[2];
			values[0] = new HandleValue(2, Util.encodeString("URL"), Util.encodeString(handle.getURL()));
			values[1] = handle.getHandleAdmin();
			byte[] handleBytes = Util.encodeString(handle.getName());
			CreateHandleRequest request = new CreateHandleRequest(handleBytes, values, auth);
			AbstractResponse response = resolver.processRequest(request);
			return new HandleResponse(response.responseCode, response.getResponseCodeMessage(response.responseCode), this.base_url + handle.getName());
			
		}else {	
			return null;
		}
	}
	
	public HandleResponse updateHandle(Handle handle) throws HandleException {
		resolver.traceMessages = true;
		AuthenticationInfo auth = this.authenticate(adminHandle);
		if(auth != null) {
			
			byte[] handleBytes = Util.encodeString(handle.getName());
			ModifyValueRequest modify = new ModifyValueRequest(handleBytes, handle.getHandleValue(), auth);
			AbstractResponse response = resolver.processRequest(modify);
			
			return new HandleResponse(response.responseCode, response.getResponseCodeMessage(response.responseCode), this.base_url + handle.getName());
			
		}else {
			
			return null;
			
		}
	}
	
	public HandleResponse deleteHandle(Handle handle) throws HandleException {
		resolver.traceMessages = true;
		AuthenticationInfo auth = this.authenticate(adminHandle);
		if(auth != null) {
			
			byte[] handleBytes = Util.encodeString(handle.getName());
			DeleteHandleRequest delete = new DeleteHandleRequest(handleBytes, auth);
			AbstractResponse response = resolver.processRequest(delete);
			return new HandleResponse(response.responseCode, response.getResponseCodeMessage(response.responseCode), this.base_url + handle.getName());
			
		}else {
			
			return null;
			
		}
	}
	
	public List<String> listHandles() throws HandleException{
		resolver.traceMessages = true;
		AuthenticationInfo auth = this.authenticate(adminHandle);
		if(auth != null) {
			SiteInfo siteInfo = BatchUtil.getSite(adminHandle, resolver);
			ListHandlesUtil list = new ListHandlesUtil(siteInfo, auth, resolver);
			List<String> lista = list.getAllHandles(adminHandle);		
			return lista;
		}else {
			
			return null;
			
		}
	}
	
	public AuthenticationInfo authenticate(String adminHandle) {
		int adminIndex = 300;
		PrivateKey privkey = this.getPrivKey();
		if(privkey != null) {
			AuthenticationInfo authinfo = new PublicKeyAuthenticationInfo(Util.encodeString(adminHandle), adminIndex, privkey);
			return authinfo;
		}else {
			return null;
		}
	}
	
	public PrivateKey getPrivKey(){
		Resource resource = new ClassPathResource("admpriv_tmp.bin");
		File file = new File("admpriv.bin");
		PrivateKey privkey;
		try {
			InputStream inputstream = resource.getInputStream();
			try (FileOutputStream outputStream = new FileOutputStream(file)) {
                int read;
                byte[] bytes = new byte[1024];

                while ((read = inputstream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }
            }
			privkey = Util.getPrivateKeyFromFileWithPassphrase(file, "bdcvcolmex");
			file.delete();
		} catch (IOException e) {
			logger.error("Error to open File privateKey");
			e.printStackTrace();
			return null;
			
		} catch (Exception e) { 
			logger.error("Error to decrypt PrivateKey");
			e.printStackTrace();
			return null;
		}
		
		 return privkey;
	}

}
