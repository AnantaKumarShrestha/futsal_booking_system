package com.intern.futsalBookingSystem.payload;

import lombok.Data;

@Data
public class TurnOverStats {


    private double dailyTurnover;
    private double weeklyTurnover;
    private double monthlyTurnover;

    public TurnOverStats(double dailyTurnover, double weeklyTurnover, double monthlyTurnover) {
        this.dailyTurnover = dailyTurnover;
        this.weeklyTurnover = weeklyTurnover;
        this.monthlyTurnover = monthlyTurnover;
    }


}
