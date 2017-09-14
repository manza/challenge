package br.com.manzano.challenge.n26.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Statistic {

    private List<Double> amounts = new ArrayList<>();

    public Statistic() {
    }

    public Statistic(Date timestamp, List<Double> amounts) {
        this.amounts = amounts;
    }

    public List<Double> getAmounts() {
        return amounts;
    }

    public void setAmounts(List<Double> amounts) {
        this.amounts = amounts;
    }
}
