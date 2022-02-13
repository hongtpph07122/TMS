package com.tms.api.helper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;

import com.tms.api.entity.SearchCriteria;
import com.tms.api.exception.ErrorMessage;
import com.tms.api.exception.TMSException;
import com.tms.api.repository.ReportSpecification;

public class SpecificationsBuilder {
    public static <T> Specification build(List<SearchCriteria> searchParams, Class<T> type) throws TMSException {
        if (searchParams == null || searchParams.size() == 0) {
            return null;
        }
        List<Field> fields = Arrays.asList(type.getDeclaredFields());
        List<String> fieldNames = fields.stream().map(field -> field.getName()).collect(Collectors.toList());
        for (SearchCriteria searchParam : searchParams) {
            if (!fieldNames.contains(searchParam.getKey())) {
                throw new TMSException(ErrorMessage.INVALID_PARAM);
            }
        }

        List<Specification> specs = searchParams.stream().map(ReportSpecification::new).collect(Collectors.toList());
        Specification result = specs.get(0);

        for (int i = 1; i < searchParams.size(); i++) {
            result = Specification.where(result).and(specs.get(i));
        }
        return result;
    }
}
