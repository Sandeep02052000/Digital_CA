package org.tax.mitra.util;

import io.netty.util.internal.StringUtil;
import org.tax.mitra.exception.GstException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class IdGeneratorUtil {
    public static String generateUniqueId(String prefix) {
        String datePart = LocalDate.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        String randomPart = UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 6)
                .toUpperCase();

        return String.format("TM-%s-%s-%s", prefix, datePart, randomPart);
    }
}
