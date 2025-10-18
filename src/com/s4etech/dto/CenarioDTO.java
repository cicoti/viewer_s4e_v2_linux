package com.s4etech.dto;

import java.util.ArrayList;
import java.util.List;

public class CenarioDTO {

    private String nomeCenario;
    private List<String> codigoCameras;

    // Construtor padrão, exigido pelo Jackson para deserializar
    public CenarioDTO() {
        // Inicializa a lista para evitar NullPointerExceptions
        this.codigoCameras = new ArrayList<>();
    }

    // Construtor que você já tinha
    public CenarioDTO(String nomeCenario) {
        this.nomeCenario = nomeCenario;
        this.codigoCameras = new ArrayList<>();
    }

    public String getNomeCenario() {
        return nomeCenario;
    }

    public void setNomeCenario(String nomeCenario) {
        this.nomeCenario = nomeCenario;
    }

    public List<String> getCodigoCameras() {
        return codigoCameras;
    }

    public void setCodigoCameras(List<String> codigoCameras) {
        this.codigoCameras = codigoCameras;
    }

    public void adicionarCamera(String codigo) {
        if (!codigoCameras.contains(codigo)) {
            codigoCameras.add(codigo);
        }
    }

    public void removerCamera(String codigo) {
        codigoCameras.remove(codigo);
    }
}
