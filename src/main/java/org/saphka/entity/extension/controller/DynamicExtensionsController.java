package org.saphka.entity.extension.controller;

import org.saphka.entity.extension.bl.ExtensionBusinessLogic;
import org.saphka.entity.extension.model.ExtensionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "${entity.extension.controller.path:/parameters/entity/extensions}")
public class DynamicExtensionsController {

	private final ExtensionBusinessLogic businessLogic;

	@Autowired
	public DynamicExtensionsController(ExtensionBusinessLogic businessLogic) {
		this.businessLogic = businessLogic;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<ExtensionDTO>> getAllExtensions() {
		return ResponseEntity.ok(businessLogic.getRegisteredExtensions());
	}

	@RequestMapping(method = RequestMethod.GET, path = "/{id:[\\w.]+}")
	public ResponseEntity<ExtensionDTO> getExtensionById(@PathVariable("id") String extensionId) {
		return ResponseEntity.of(businessLogic.getRegisteredExtension(extensionId));
	}


}
