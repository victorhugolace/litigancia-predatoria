package br.jus.tjpb.lyno.litigancia.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public enum Periodo {
    MES, ANO, TODOS;

    public LocalDateTime inicio(LocalDate hoje) {
        return switch (this) {
            case MES -> hoje.withDayOfMonth(1).atStartOfDay();
            case ANO -> hoje.withDayOfYear(1).atStartOfDay();
            case TODOS -> LocalDate.of(1970, 1, 1).atStartOfDay();
        };
    }
}
