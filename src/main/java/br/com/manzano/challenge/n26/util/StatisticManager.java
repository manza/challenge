package br.com.manzano.challenge.n26.util;

import br.com.manzano.challenge.n26.model.Statistic;
import br.com.manzano.challenge.n26.model.Transaction;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class StatisticManager {

    private ConcurrentMap<Integer, Statistic> statsFromLastMinute;

    public StatisticManager() {
        statsFromLastMinute = new ConcurrentHashMap<>();
        for(int i = 1; i <= 60; i++) {
            statsFromLastMinute.put(i, new Statistic(0, 0, 0, 0, 0));
        }
    }

    public void persistNewTransaction(Transaction transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("Invalid Transaction");
        }

        LocalDateTime dateTime = LocalDateTime.ofEpochSecond(transaction.getTimestamp().toInstant().getEpochSecond(), 0, ZoneOffset.UTC);
        int givenSecond = dateTime.getSecond();

        Statistic givenStat = (Statistic)statsFromLastMinute.get(givenSecond);

        givenStat.setCnt(manageCnt(givenStat, transaction));
        givenStat.setSum(manageSum(givenStat, transaction));
        givenStat.setAvg(manageAvg(givenStat, transaction));
        givenStat.setMin(manageMin(givenStat, transaction));
        givenStat.setMax(manageMax(givenStat, transaction));

        statsFromLastMinute.put(givenSecond, givenStat);
    }

    private synchronized double manageSum(Statistic statistic, Transaction transaction) {
        double newSum = statistic.getSum();
        newSum = newSum + transaction.getAmount();

        return newSum;
    }

    private synchronized double manageAvg(Statistic statistic, Transaction transaction) {
        if (statistic.getCnt() <= 1) {
            return transaction.getAmount();
        }

        double newAvg = statistic.getSum() / statistic.getCnt();

        return newAvg;
    }

    private synchronized double manageMin(Statistic statistic, Transaction transaction) {
        if (statistic.getCnt() <= 1) {
            return transaction.getAmount();
        }

        double curMin = statistic.getMin();
        if (transaction.getAmount() < curMin) {
            curMin = transaction.getAmount();
        }

        return curMin;
    }

    private synchronized double manageMax(Statistic statistic, Transaction transaction) {
        if (statistic.getCnt() <= 1) {
            return transaction.getAmount();
        }

        double curMax = statistic.getMax();
        if (transaction.getAmount() > curMax) {
            curMax = transaction.getAmount();
        }

        return curMax;
    }

    private synchronized long manageCnt(Statistic statistic, Transaction transaction) {
        long curCnt = statistic.getCnt();
        curCnt = curCnt + 1;

        return curCnt;
    }

    public double getSum() {
        double sum = 0;
        for(Statistic statistic : statsFromLastMinute.values()) {
            if (statistic.getSum() > 0) {
                sum = sum + statistic.getSum();
            }
        }

        return sum;
    }

    public double getAvg() {
        return getCount() > 1 ? getSum() / getCount() : 0;
    }

    public double getMin() {
        double min = Double.MAX_VALUE;
        for(Statistic statistic : statsFromLastMinute.values()) {
            if (statistic.getMin() > 0) {
                if (statistic.getMin() < min) {
                    min = statistic.getMin();
                }
            }
        }

        return min == Double.MAX_VALUE ? 0 : min;
    }

    public double getMax() {
        double max = Double.MIN_VALUE;
        for(Statistic statistic : statsFromLastMinute.values()) {
            if (statistic.getMax() > 0) {
                if (statistic.getMax() > max) {
                    max = statistic.getMax();
                }
            }
        }

        return max == Double.MIN_VALUE ? 0 : max;
    }

    public long getCount() {
        long count = 0;
        for(Statistic statistic : statsFromLastMinute.values()) {
            count = count + statistic.getCnt();
        }
        return count;
    }
}
