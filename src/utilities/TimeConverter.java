package utilities;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class TimeConverter {

    public static LocalDateTime toLocalTZ(LocalDateTime ldt)  {
        ZonedDateTime zdt = ldt.atZone(ZoneId.of("UTC")); // set ldt to utc tz (used in db)
        ZonedDateTime zdtLocal = zdt.withZoneSameInstant(ZoneId.of(ZoneId.systemDefault().toString())); // set time to local tz equivalent
        LocalDateTime ltz = zdtLocal.toLocalDateTime();
        return ltz;
    }

    public static LocalDateTime toUTC(LocalDateTime ldt)  {

        ZonedDateTime zdt = ldt.atZone(ZoneId.of(ZoneId.systemDefault().toString())); // bind current time to system's default tz
        ZonedDateTime utcZD = zdt.withZoneSameInstant(ZoneId.of("UTC")); // set time to utc equivalent
        LocalDateTime utcLDT = utcZD.toLocalDateTime(); // convert to ldt type
        return utcLDT;
    }

}
