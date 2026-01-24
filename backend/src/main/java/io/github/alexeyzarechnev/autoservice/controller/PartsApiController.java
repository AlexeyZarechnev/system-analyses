package io.github.alexeyzarechnev.autoservice.controller;

import io.github.alexeyzarechnev.autoservice.dto.PartDTO;
import io.github.alexeyzarechnev.autoservice.model.Part;
import io.github.alexeyzarechnev.autoservice.service.PartService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.context.request.NativeWebRequest;

import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import jakarta.annotation.Generated;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-24T19:30:52.598661+03:00[Europe/Moscow]", comments = "Generator version: 7.8.0")
@Controller
@RequestMapping("${openapi.autoservice.base-path:}")
public class PartsApiController implements PartsApi {

    private final NativeWebRequest request;

    private final PartService partsService;

    @Autowired
    public PartsApiController(NativeWebRequest request, PartService partsService) {
        this.request = request;
        this.partsService = partsService;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    public ResponseEntity<PartDTO> createPart(@Valid PartDTO partDTO) {
        Part part = fromDto(partDTO);
        Part savedPart = partsService.savePart(part);
        if (savedPart == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        PartDTO savedPartDTO = toDto(savedPart);
        return new ResponseEntity<>(savedPartDTO, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<PartDTO> getPartById(Long id) {
        return partsService.getPartById(id) != null ? 
            new ResponseEntity<>(toDto(partsService.getPartById(id)), HttpStatus.OK) :
            new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<PartDTO> updatePart(Long id, @Valid PartDTO partDTO) {
        Part updatedPartData = fromDto(partDTO);
        Part updatedPart = partsService.updatePart(id, updatedPartData);
        if (updatedPart == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        PartDTO updatedPartDTO = toDto(updatedPart);
        return new ResponseEntity<>(updatedPartDTO, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deletePart(Long id) {
        if (partsService.deletePart(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    private Part fromDto(PartDTO partDTO) {
        Part part = new Part();
        part.setName(partDTO.getName());
        part.setArticleNumber(partDTO.getArticleNumber());
        part.setRemains(partDTO.getRemains().shortValue());
        part.setPrice(partDTO.getPrice());
        return part;
    }

    private PartDTO toDto(Part part) {
        return new PartDTO()
            .id(part.getId())
            .name(part.getName())
            .articleNumber(part.getArticleNumber())
            .remains(Short.toUnsignedInt(part.getRemains()))
            .price(part.getPrice());
    }

}
