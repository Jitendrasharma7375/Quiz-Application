package com.smartdocs.quiz_service.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class Response {
	
	public Response() {

	}
	
	public Response(Integer id, String response) {
		super();
		this.id = id;
		this.response = response;
	}
	private Integer id;
	private String response;
}
