package com.handle.api.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.PrivateKey;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.handle.api.model.Handle;
import com.handle.api.model.HandleResponse;
import com.handle.api.repositories.entities.Secuence_names;

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

@Service("handleService")
public class HandleServiceImpl implements HandleService{
	
	@Autowired	
	private SecuenceService secuenceService;	
	private static final Logger logger = LogManager.getLogger(HandleServiceImpl.class);
	private String base_url = "http://hdl.handle.net/";
	private String adminHandle = "0.NA/20.500.11986";
	private HandleResolver resolver = new HandleResolver();
	
	
	@Override
	public HandleResponse beforeCreate(Handle handle) throws HandleException {
		String tmpname = handle.getName();
		HandleResponse response = new HandleResponse();
		if(tmpname != null && !tmpname.trim().equals("")) {
			if(tmpname.contains("/") && tmpname.contains("20.500.11986")) {
				if(!(tmpname.substring(tmpname.length()-1, tmpname.length())).equals("/")) {
					tmpname = tmpname + "/";
				}
			}else {
				return new HandleResponse(0, null, null, "ERROR", "El nombre debe contener el prefijo identificador de la institución (00.000.00000) y al menos un slash (/).");
			}
			
			if(handle.getIdHandle() != null && !handle.getIdHandle().equals("")) {
				boolean existHandle = existHandle(handle);
				if(existHandle) {
					Secuence_names newSecuence = getSecuence();
					handle.setName(tmpname + newSecuence.getSecuence());
					response = this.createHandle(handle);
					if(response.getHandleCode() == 1) {
						newSecuence =  secuenceService.save(newSecuence);
					}					
					return response;
					//return new HandleResponse(0, null, null, "ERROR", "El id especificado ya existe.");
				}else {
					handle.setName(tmpname + handle.getIdHandle());
					response = this.createHandle(handle);
					if(response.getHandleCode() == 1) {
						int secuence = Integer.parseInt(handle.getIdHandle());
						Secuence_names tmpSecuence = secuenceService.findBySecuence(secuence);
						if(tmpSecuence == null) {
							tmpSecuence = new Secuence_names();
							tmpSecuence.setSecuence(secuence);
							tmpSecuence.setStatus(1);
							tmpSecuence = secuenceService.save(tmpSecuence);
						}
					}
					return response;
				}
			}else {
				Secuence_names newSecuence = getSecuence();
				handle.setName(tmpname + newSecuence.getSecuence());
				response = this.createHandle(handle);
				if(response.getHandleCode() == 1) {
					newSecuence = secuenceService.save(newSecuence);
				}				
				return response;
			}
		}else {
			return new HandleResponse(0, null, null, "ERROR", "Se debe especificar el nombre del handle.");
		}
	}
	
	@Override
	public HandleResponse updateHandle(Handle handle) throws HandleException {
		resolver.traceMessages = true;
		AuthenticationInfo auth = this.authenticate(adminHandle);
		if(auth != null) {
			
			byte[] handleBytes = Util.encodeString(handle.getName());
			ModifyValueRequest modify = new ModifyValueRequest(handleBytes, handle.getHandleValue(), auth);
			AbstractResponse response = resolver.processRequest(modify);
			
			return new HandleResponse(response.responseCode, response.getResponseCodeMessage(response.responseCode), this.base_url + handle.getName(), "SUCCESS", "");
			
		}else {			
			return new HandleResponse(0, null, null, "ERROR", "Ocurrio un error al autenticarse en el servidor Handle.");
		}
	}
	
	
	@Override
	public HandleResponse deleteHandle(String hdl, String collection, String handleId) throws HandleException {
		Handle handle = new Handle();
		if(!hdl.trim().equals("") && !collection.trim().equals("") && !handleId.trim().equals("")) {
			handle.setName(hdl + "/" + collection + "/" + handleId);
		}		
		
		int secuence = Integer.parseInt(handleId);
		SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String fecha = outputFormat.format(new Date());
		Secuence_names secuenceData = secuenceService.findBySecuence(secuence);
		secuenceData.setModified(fecha);
		secuenceData.setStatus(0);
		if(handle.getName().contains("/") && handle.getName().contains("20.500.11986")) {
			resolver.traceMessages = true;
			AuthenticationInfo auth = this.authenticate(adminHandle);
			if(auth != null) {				
				byte[] handleBytes = Util.encodeString(handle.getName());
				DeleteHandleRequest delete = new DeleteHandleRequest(handleBytes, auth);
				AbstractResponse response = resolver.processRequest(delete);
				if(response.responseCode == 1) {
					secuenceService.save(secuenceData);
				}
				return new HandleResponse(response.responseCode, response.getResponseCodeMessage(response.responseCode), this.base_url + handle.getName(),"SUCCESS", "");				
			}else {			
				return new HandleResponse(0, null, null, "ERROR", "Ocurrio un error al autenticarse en el servidor Handle.");			
			}
		}else {
			return new HandleResponse(0, null, null, "ERROR", "El nombre debe contener el prefijo identificador de la institución (00.000.00000) y al menos dos slash (/).");
		}		
	}
	
	@Override
	public List<String> listHandles() throws HandleException{
		resolver.traceMessages = true;
		AuthenticationInfo auth = this.authenticate(adminHandle);
		if(auth != null) {
			SiteInfo siteInfo = BatchUtil.getSite(adminHandle, resolver);
			ListHandlesUtil list = new ListHandlesUtil(siteInfo, auth, resolver);
			List<String> lista = list.getAllHandles(adminHandle);
			
			/***
			 * Esta parte se puede utilizar para comprobar si existe otro Handle con el mismo url del repositorio
			 * 
			 */
			/*list.getMatchingHandles(new HandleRecordFilter() {
				
				@Override
				public boolean accept(HandleValue[] arg0) {
					// TODO Auto-generated method stub
					arg0[0]
					return false;
				}
			}, adminHandle);*/
			return lista;
		}else {			
			return null;
		}
	}
	
	private HandleResponse createHandle(Handle handle) throws HandleException {
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
			return new HandleResponse(response.responseCode, response.getResponseCodeMessage(response.responseCode), this.base_url + handle.getName(), "SUCCESS", "");
		}else {
			return new HandleResponse(0, null, null, "ERROR", "Ocurrio un error al autenticarse en el servidor Handle.");
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
			privkey = Util.getPrivateKeyFromFileWithPassphrase(file, "secretPassPhrase");
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
	
	private boolean existHandle(Handle handle) throws HandleException {
		List<String> handles = listHandles();
		String handleName = handle.getName() + handle.getIdHandle();
		//20.500.11986/COLMEX/10014088
		Boolean exist = handles.stream().anyMatch(value -> handleName.equals(value));
		if(exist) {			
			return true;
		}else {
			return false;
		}
	}
	
	private Secuence_names getSecuence() {
		Secuence_names secuence = secuenceService.getInactive();
	    SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	    String fecha = outputFormat.format(new Date());
		if(secuence == null) {
			secuence = secuenceService.getMax();
			int number = secuence.getSecuence();
			Secuence_names newSecuence = new Secuence_names();
			newSecuence.setSecuence(number + 1);
			newSecuence.setStatus(1);
			newSecuence.setCreated(fecha);
			return newSecuence;
		}else {
			secuence.setStatus(1);
			secuence.setModified(fecha);
			return secuence;
		}
	}
	
}
