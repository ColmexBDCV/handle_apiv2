package com.handle.api.services;

import java.util.List;

import com.handle.api.model.Handle;
import com.handle.api.model.HandleResponse;

import net.handle.hdllib.HandleException;

public interface HandleService {

	HandleResponse beforeCreate(Handle handle) throws HandleException;

	HandleResponse updateHandle(Handle handle) throws HandleException;

	HandleResponse deleteHandle(String hdl, String collection, String handleId) throws HandleException;

	List<String> listHandles() throws HandleException;

}
