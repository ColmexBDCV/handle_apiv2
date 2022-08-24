package com.handle.api.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.handle.api.entities.Handle;
import com.handle.api.entities.HandleResponse;
import com.handle.api.entities.Secuence_names;
import com.handle.api.services.HandleOperation;
import com.handle.api.services.SecuenceService;

import net.handle.hdllib.HandleException;

@RestController
@RequestMapping("/handle")
public class HandlesREST {
	private HandleOperation operations = new HandleOperation();
	
	@Autowired
	private SecuenceService secuenceService;
	
	@RequestMapping(value="/create", method = RequestMethod.POST)
	public ResponseEntity<HandleResponse> createHandle(@RequestBody Handle handleRequest){
		HandleResponse handleRes = null;
		String tmpname = handleRequest.getName();
		//System.out.println("URL: " + handleRequest.getURL());

		if(!(tmpname.substring(tmpname.length()-1, tmpname.length())).equals("/")) {
			tmpname = tmpname + "/";
		}

		try {
			Secuence_names secuence = secuenceService.getInactive();
			if(secuence == null) {
				secuence = secuenceService.getMax();
				int number = secuence.getSecuence();
				Secuence_names newSecuence = new Secuence_names();
				newSecuence.setSecuence(number + 1);
				handleRequest.setName(tmpname + newSecuence.getSecuence());
				newSecuence.setStatus(1);
				handleRes = operations.createHandle(handleRequest);
				if(handleRes.getHandleCode() == 1) {
					secuence = secuenceService.save(newSecuence);
				}
			}else {
				handleRequest.setName(tmpname + secuence.getSecuence());
				handleRes = operations.createHandle(handleRequest);
				secuence.setStatus(1);
				if(handleRes.getHandleCode() == 1) {
					secuence = secuenceService.save(secuence);
				}
			}
			
		} catch (HandleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.ok(handleRes);
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public ResponseEntity<HandleResponse> modifyHandle(@RequestBody Handle handleRequest){
		HandleResponse handleRes = null;
		try {
			handleRes = operations.updateHandle(handleRequest);
		} catch (HandleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.ok(handleRes);
	}
	
	@RequestMapping(value = "/delete/{handle}/{collection}/{handleId}", method = RequestMethod.DELETE)
	public ResponseEntity<HandleResponse> deleteHandle(@PathVariable("handle") String handle, @PathVariable("collection") String collection ,@PathVariable("handleId") String handleId){
		HandleResponse handleRes = null;
		Handle handleRequest = new Handle();
		handleRequest.setName(handle + "/" + collection + "/" + handleId);
		int secuence = Integer.parseInt(handleId);
		Secuence_names secuenceData = secuenceService.findBySecuence(secuence);
		secuenceData.setStatus(0);
		try {
			handleRes = operations.deleteHandle(handleRequest);
			secuenceService.save(secuenceData);
		} catch (HandleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.ok(handleRes);
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ResponseEntity<List<String>> listHandle(){
		List<String> lista = null;
		try {
			
			lista = operations.listHandles();
		} catch (HandleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.ok(lista);
	}
	
	//@GetMapping
	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	public ResponseEntity<String> hello() {		
		/*for(Secuence_names secuence : secuenceService.findAll()) {
			System.out.println("Id: " + secuence.getId_secuence());
			System.out.println("Name: " + secuence.getSecuence());
		}
		
		Secuence_names secuence = secuenceService.getMax();
		System.out.println("Max Value: " + secuence.getSecuence());
		int number = secuence.getSecuence();
		Secuence_names newSecuence = new Secuence_names();
		newSecuence.setSecuence(number + 1);
		secuence = secuenceService.save(newSecuence);*/
		return ResponseEntity.ok("Hello World");
	}

}
