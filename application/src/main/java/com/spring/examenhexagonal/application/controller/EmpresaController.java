package com.spring.examenhexagonal.application.controller;

import com.spring.examenhexagonal.domain.aggregates.dto.EmpresaDTO;
import com.spring.examenhexagonal.domain.aggregates.dto.EmpresaRequestDTO;
import com.spring.examenhexagonal.domain.aggregates.dto.ResponseBase;
import com.spring.examenhexagonal.domain.ports.in.EmpresaServiceIn;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/empresa")
@RequiredArgsConstructor
public class EmpresaController {

    private final EmpresaServiceIn empresaServiceIn;

    @GetMapping("/{id}")
    public ResponseEntity<ResponseBase<EmpresaDTO>> getEmpresaById(@PathVariable Long id) {
            ResponseBase<EmpresaDTO> empresaDTO = empresaServiceIn.getByIdIn(id);
            return ResponseEntity.ok(empresaDTO);
    }

    @GetMapping
    public ResponseEntity<ResponseBase<List<EmpresaDTO>>> getAllEmpresa() {
            ResponseBase<List<EmpresaDTO>> empresasDTO = empresaServiceIn.getAllIn();
            return ResponseEntity.ok(empresasDTO);
    }

    @PostMapping("/saveByFeingClient")
    public ResponseEntity<ResponseBase<EmpresaDTO>> createEmpresaByFeingClient(@RequestParam ("numeroDocumento")String numeroDocument) {
        ResponseBase<EmpresaDTO> empresaDTOResponseBase = empresaServiceIn.saveIn(numeroDocument);
        return new ResponseEntity<>(empresaDTOResponseBase, HttpStatus.CREATED);
    }

    @PostMapping("/saveByRestTemplate")
    public ResponseEntity<ResponseBase<EmpresaDTO>> createEmpresaByRestTemplate(@RequestParam ("numeroDocumento")String numeroDocument) {
        ResponseBase<EmpresaDTO> empresaDTOResponseBase = empresaServiceIn.saveInByRestTemplate(numeroDocument);
        return new ResponseEntity<>(empresaDTOResponseBase, HttpStatus.CREATED);
    }

    @PostMapping("/saveByRetrofit")
    public ResponseEntity<ResponseBase<EmpresaDTO>> createByRetrofit(@RequestParam ("numeroDocumento")String numeroDocument) throws IOException {
        ResponseBase<EmpresaDTO> empresaDTOResponseBase = empresaServiceIn.saveInByRetrofit(numeroDocument);
        return new ResponseEntity<>(empresaDTOResponseBase, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseBase<EmpresaDTO>> updateEmpresa(@PathVariable Long id, @Valid @RequestBody EmpresaRequestDTO empresaRequestDTO) {
        ResponseBase<EmpresaDTO> empresaDTO = empresaServiceIn.updateIn(id,empresaRequestDTO);
        return ResponseEntity.ok(empresaDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseBase<String>> deleteEmpresa(@PathVariable Long id) {
            empresaServiceIn.deleteIn(id);
            return ResponseEntity.noContent().build();
    }

}
