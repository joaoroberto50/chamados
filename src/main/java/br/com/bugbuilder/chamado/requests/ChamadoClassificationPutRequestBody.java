package br.com.bugbuilder.chamado.requests;

import java.util.UUID;

import br.com.bugbuilder.chamado.models.ClassificationName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChamadoClassificationPutRequestBody {
	private UUID id;
	@Schema(description = "Classification name has to be one of the following ways: 'MUITO_URGENTE', 'URGENTE' or 'POUCO_URGENTE'",
			example = "URGENTE", required = true)
	private ClassificationName classificationName;
}
