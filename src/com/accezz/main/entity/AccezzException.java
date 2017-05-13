package com.accezz.main.entity;

public class AccezzException extends RuntimeException {
	/**
	 *
	 */
	private static final long serialVersionUID = 1175967709497623882L;

	public AccezzException(String s) {
		super(s);
	}

	public AccezzException(Exception e) {
		super(e);
	}
}
