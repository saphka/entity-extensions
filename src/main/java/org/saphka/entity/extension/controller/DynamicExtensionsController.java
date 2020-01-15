package org.saphka.entity.extension.controller;

import org.saphka.entity.extension.bl.ExtensionBusinessLogic;
import org.saphka.entity.extension.model.ExtensionDTO;
import org.saphka.entity.extension.model.FieldDTO;
import org.saphka.entity.extension.model.NewFieldDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Alex Loginov
 */
@RestController
@RequestMapping(path = "${entity.extension.controller.path:/parameters/entity/extensions}")
public class DynamicExtensionsController {

	private final ExtensionBusinessLogic businessLogic;
	private final String controllerPath;

	@Autowired
	public DynamicExtensionsController(ExtensionBusinessLogic businessLogic,
									   @Value("${entity.extension.controller.path:/parameters/entity/extensions}") String controllerPath) {
		this.businessLogic = businessLogic;
		this.controllerPath = controllerPath;
	}

	@GetMapping
	public ResponseEntity<List<ExtensionDTO>> getAllExtensions() {
		return ResponseEntity.ok(businessLogic.getRegisteredExtensions());
	}

	@GetMapping(path = "/{id:[\\w.]+}")
	public ResponseEntity<ExtensionDTO> getExtensionById(@PathVariable("id") String extensionId) {
		return ResponseEntity.of(businessLogic.getRegisteredExtension(extensionId));
	}

	@PostMapping
	public ResponseEntity<FieldDTO> createNewExtension(@RequestBody @Valid NewFieldDTO fieldDTO, UriComponentsBuilder ucb) {
		FieldDTO createdField = businessLogic.createExtension(fieldDTO);

		return ResponseEntity.created(ucb.path(controllerPath + "/fields/{id}").buildAndExpand(createdField.getId()).toUri()).body(createdField);
	}


}
