package com.sroka.grouptripsorganizer.dto.date_time;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

import static com.sroka.grouptripsorganizer.dto.date_time.DateTimeFormat.LOCAL_DATE_PATTERN;
import static org.springframework.format.annotation.DateTimeFormat.ISO;

@Getter
@Setter
@AllArgsConstructor
public class DatePeriodDto {

    @NotNull(message = "error.cannotBeBlank")
    @JsonFormat(pattern = LOCAL_DATE_PATTERN)
    @DateTimeFormat(iso = ISO.DATE)
    @Schema(required = true)
    private LocalDate startDate;

    @NotNull(message = "error.cannotBeBlank")
    @JsonFormat(pattern = LOCAL_DATE_PATTERN)
    @DateTimeFormat(iso = ISO.DATE)
    @Schema(required = true)
    private LocalDate endDate;
}