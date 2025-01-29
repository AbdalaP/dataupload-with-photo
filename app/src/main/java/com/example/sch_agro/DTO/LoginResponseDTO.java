package com.example.sch_agro.DTO;

public class LoginResponseDTO {
    private String token;
    private Long userId;
    private String nome;
    private String email;
    private String role;

    public LoginResponseDTO(String token, Long userId, String nome, String email, String role) {
        this.token = token;
        this.userId = userId;
        this.nome = nome;
        this.email = email;
        this.role = role;
    }

    public LoginResponseDTO() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
