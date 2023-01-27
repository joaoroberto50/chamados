package br.com.bugbuilder.chamado.requests;


import br.com.bugbuilder.chamado.models.ClassificationName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChamadoClassificationPutRequestBody {
	@Schema(description = "Classification name has to be one of the following ways: 'MUITO_URGENTE', 'URGENTE' or 'POUCO_URGENTE'",
			example = "URGENTE", required = true)
	private ClassificationName classificationName;

}
