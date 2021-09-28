package com.sroka.grouptripsorganizer.dto.date_time;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDateTime;

import static com.sroka.grouptripsorganizer.dto.date_time.DateTimeFormat.LOCAL_DATE_TIME_PATTERN;

@Getter
@Setter
@AllArgsConstructor
public class DateTimePeriodDto {

    @NotNull(message = "error.cannotBeBlank")
    @JsonFormat(pattern = LOCAL_DATE_TIME_PATTERN)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Schema(required = true)
    private LocalDateTime startDate;

    @NotNull(message = "error.cannotBeBlank")
    @JsonFormat(pattern = LOCAL_DATE_TIME_PATTERN)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Past(message = "error.onlyPastDates")
    @Schema(required = true)
    private LocalDateTime endDate;
}
