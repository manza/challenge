package br.com.manzano.challenge.n26.util;

import br.com.manzano.challenge.n26.model.Statistic;
import br.com.manzano.challenge.n26.model.Transaction;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.TemporalField;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.OptionalDouble;

@Component
public class StatisticManager {

    private Map<Integer, Statistic> statsFromLastMinute;

    public StatisticManager() {
        statsFromLastMinute = new HashMap<>();

        for(int i = 1; i <= 60; i++) {
            statsFromLastMinute.put(i, new Statistic(new Date(), new ArrayList<>(0)));
        }
    }

    public void persistNewTransaction(Transaction transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("Invalid Transaction");
        }

        LocalDateTime dateTime = LocalDateTime.ofEpochSecond(transaction.getTimestamp().getEpochSecond(), 0, ZoneOffset.UTC);
        int givenSecond = dateTime.getSecond();

        Statistic givenStat = (Statistic)statsFromLastMinute.get(givenSecond);

        givenStat.getAmounts().add(transaction.getAmount());
    }

    public double getSum() {
        double sum = 0;
        for(Statistic statistic : statsFromLastMinute.values()) {
            sum = sum + statistic.getAmounts().stream().mapToDouble(Double::doubleValue).sum();
        }

        return sum;
    }

    public double getAvg() {
        double avg = 0;
        for(Statistic statistic : statsFromLastMinute.values()) {
            OptionalDouble optionalDouble = statistic.getAmounts().stream().mapToDouble(Double::doubleValue).average();
            if (optionalDouble.isPresent()) {
                avg = avg + optionalDouble.getAsDouble();
            }
        }

        return (avg > 0) ? (avg / 60.0) : 0;
    }

    public double getMin() {
        double min = Double.MAX_VALUE;
        for(Statistic statistic : statsFromLastMinute.values()) {
            OptionalDouble optionalDouble = statistic.getAmounts().stream().mapToDouble(Double::doubleValue).min();
            if (optionalDouble.isPresent()) {
                if (optionalDouble.getAsDouble() < min) {
                    min = optionalDouble.getAsDouble();
                }
            }
        }

        return min == Double.MAX_VALUE ? 0 : min;
    }

    public double getMax() {
        double max = Double.MIN_VALUE;
        for(Statistic statistic : statsFromLastMinute.values()) {
            OptionalDouble optionalDouble = statistic.getAmounts().stream().mapToDouble(Double::doubleValue).max();
            if (optionalDouble.isPresent()) {
                if (optionalDouble.getAsDouble() > max) {
                    max = optionalDouble.getAsDouble();
                }
            }
        }
        return max == Double.MIN_VALUE ? 0 : max;
    }

    public long getCount() {
        long count = 0;
        for(Statistic statistic : statsFromLastMinute.values()) {
            count = count + statistic.getAmounts().size();
        }
        return count;
    }
}
