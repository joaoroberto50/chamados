package br.com.bugbuilder.chamado.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


public class Creator {

	public static PasswordEncoder pass() {
		return new BCryptPasswordEncoder();
	}
	
	public static void main(String[] args) {
		System.out.println(pass().encode("10Test"));
	}
	
}
