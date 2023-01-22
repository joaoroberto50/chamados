package br.com.bugbuilder.chamado.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

@Component
public class DateTimeUtil {
	public String formatLocalDateTimeBaseStyle(LocalDateTime localDateTime) {
		return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(localDateTime);
	}
}
