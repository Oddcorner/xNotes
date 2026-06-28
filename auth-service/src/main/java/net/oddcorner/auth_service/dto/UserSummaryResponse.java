package net.oddcorner.auth_service.dto;

import java.util.Set;

public record UserSummaryResponse(
    String username,
    String email,
    String password,
    Set<String> roles
    ) {};
