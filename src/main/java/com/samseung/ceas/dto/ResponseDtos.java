package com.samseung.ceas.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDtos<T> {
	private String error;
	private List<T> data;
	private ResponseMessage code;
}
