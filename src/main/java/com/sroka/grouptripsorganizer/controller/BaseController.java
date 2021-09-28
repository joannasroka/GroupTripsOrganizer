package com.sroka.grouptripsorganizer.controller;

import com.sroka.grouptripsorganizer.dto.date_time.DateTimePeriodDto;
import com.sroka.grouptripsorganizer.exception.NumberOfItemsPerPageExceededLimitException;
import com.sroka.grouptripsorganizer.exception.UnsupportedSortParameterException;
import com.sroka.grouptripsorganizer.exception.ValidationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.*;

public abstract class BaseController extends BaseExceptionController {

    protected void pageSizeValidation(Pageable pageable, int maxAllowedItemsPerPage) {
        if (pageable.getPageSize() > maxAllowedItemsPerPage) {
            throw new NumberOfItemsPerPageExceededLimitException();
        }
    }

    protected void sortParametersValidation(Pageable pageable, Set<String> allowedSortingParams) {
        boolean allSortingParamsAllowed = pageable.getSort().get()
                .map(Order::getProperty)
                .allMatch(allowedSortingParams::contains);

        if (!allSortingParamsAllowed) {
            throw new UnsupportedSortParameterException();
        }
    }

    protected Pageable ignoreCase(Pageable pageable) {
        Map<String, Direction> allSortingParams = pageable.getSort().get()
                .collect(Collectors.toMap(Order::getProperty, Order::getDirection));

        List<Order> orders = new ArrayList<>(allSortingParams.keySet()).stream()
                .map(param -> new Order(allSortingParams.get(param), param).ignoreCase())
                .collect(Collectors.toList());

        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), by(orders));
    }

    protected void timePeriodValidation(DateTimePeriodDto dateTimePeriodDto, int maxAllowedTimePeriodInDays, Errors errors) {
        LocalDateTime startDate = dateTimePeriodDto.getStartDate();
        LocalDateTime endDate = dateTimePeriodDto.getEndDate();

        if (startDate.isAfter(endDate)) {
            errors.rejectValue("startDate", "error.startDateAfterEndDate", "error.startDateAfterEndDate");
            errors.rejectValue("endDate", "error.startDateAfterEndDate", "error.startDateAfterEndDate");
        }

        long daysBetweenDates = startDate.until(endDate, ChronoUnit.DAYS);

        if (daysBetweenDates > maxAllowedTimePeriodInDays) {
            errors.rejectValue("endDate", "error.exceededMaxPeriod", "error.exceededMaxPeriod");
        }

        if (errors.hasErrors()) {
            throw new ValidationException(errors);
        }
    }
}
