package br.com.bugbuilder.chamado.requests;

import javax.validation.constraints.NotEmpty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ChamadoPostRequestBody {
	@NotEmpty(message = "Title cannot be empty or null")
	@Schema(description = "Title of the call", example = "Call 01", required = true)
	private String title;
	@Schema(description = "Description of the call", example = "Some text explaining the reason for the call", required = true)
	private String description;
}
