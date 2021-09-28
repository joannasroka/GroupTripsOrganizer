package com.sroka.grouptripsorganizer.controller.sorting_pagination;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Parameters({
    @Parameter(in = ParameterIn.QUERY, name = "page", schema = @Schema(type = "integer", format = "int16", defaultValue = "0", minimum = "0"), description = "Page number you want to receive."),
    @Parameter(in = ParameterIn.QUERY, name = "size", schema = @Schema(type = "integer", format = "int16", defaultValue = "25", minimum = "1", maximum = "25"), description = "Number of elements per page."),
    @Parameter(in = ParameterIn.QUERY, name = "sort", array = @ArraySchema(schema = @Schema(type = "string")), description = "Sorting items according to the given attribute(s) and order in the format: attribute(,attribute),asc|desc. "
        + "Default sorting order is ascending. ")})
public @interface CustomPageable {
}