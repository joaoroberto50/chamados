package br.com.bugbuilder.chamado.requests;

import java.util.UUID;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChamadoClosingPutRequestBody {
	private UUID id;
}
