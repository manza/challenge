package br.com.manzano.challenge.n26.model;

public class Statistic {

    private double sum;
    private double avg;
    private double min;
    private double max;
    private long cnt;

    public Statistic() {
    }

    public Statistic(double sum, double avg, double min, double max, long cnt) {
        this.sum = sum;
        this.avg = avg;
        this.min = min;
        this.max = max;
        this.cnt = cnt;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public double getAvg() {
        return avg;
    }

    public void setAvg(double avg) {
        this.avg = avg;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public long getCnt() {
        return cnt;
    }

    public void setCnt(long cnt) {
        this.cnt = cnt;
    }
}
