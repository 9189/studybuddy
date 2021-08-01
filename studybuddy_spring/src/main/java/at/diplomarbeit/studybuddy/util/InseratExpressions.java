package at.diplomarbeit.studybuddy.util;

import at.diplomarbeit.studybuddy.data.entity.QInserat;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.time.temporal.ChronoUnit;
import java.util.Date;

public class InseratExpressions {
    public static BooleanExpression hasZielStadt(Long id) {
        return QInserat.inserat.zielStadt.stadtId.eq(id);
    }

    public static BooleanExpression hasZielLand(Long id) {
        return QInserat.inserat.zielStadt.land.landId.eq(id);
    }

    public static BooleanExpression hasFromDate(Date date) {
        Date from = Date.from(date.toInstant().minus(10, ChronoUnit.DAYS));

        return QInserat.inserat.vonDat.after(from);
    }

    public static BooleanExpression hasToDate(Date date) {
        Date to = Date.from(date.toInstant().plus(10, ChronoUnit.DAYS));

        return QInserat.inserat.vonDat.before(to);
    }
}
