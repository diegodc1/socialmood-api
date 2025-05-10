package com.socialmood.socialmoodapi.dto;

import java.util.Date;

public record ReportBodyRequestDTO(Long userId, String emotion, String socialMedia, Date dateInic) {
}
