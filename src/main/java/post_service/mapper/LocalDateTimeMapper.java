package post_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public interface LocalDateTimeMapper {

    @Named("toString")
    static String toString(LocalDateTime dateTime) {
        return dateTime == null ? null
                : dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    @Named("toDateTime")
    static LocalDateTime toDateTime(String dateTime) {
        return dateTime == null ? null
                : LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
