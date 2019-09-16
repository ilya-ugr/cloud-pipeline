/*
 * Copyright 2017-2019 EPAM Systems, Inc. (https://www.epam.com/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.epam.pipeline.vo.filter.converters;


import com.epam.pipeline.vo.filter.FilterOperandType;
import com.epam.pipeline.vo.filter.WrongFilterException;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public abstract class AbstractFilterValueConverter {

    public String clearQuotes(String value) {
        if (StringUtils.isNotEmpty(value) && ((value.startsWith("\"") && value.endsWith("\"")) ||
                (value.startsWith("'") && value.endsWith("'")))) {
            return value.substring(1, value.length() - 1);
        }
        return value;
    }

    public abstract Object convert(String field,
                                   String value,
                                   FilterOperandType operandType,
                                   Map<String, Object> params)
            throws WrongFilterException;
}
