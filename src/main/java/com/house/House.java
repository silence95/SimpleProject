package com.house;

public class House {

  int years = 10;
  int totalLoans = 95_0000;
  double rateOfMonth = 0.0539 / 12;
  int periods = years * 12;
  int totalPeriods = 30 * 12;

  public double payLoansOfEachPeriod() {
    double eachLoans = totalLoans * rateOfMonth * Math.pow((1 + rateOfMonth), totalPeriods) /
                       (Math.pow((1 + rateOfMonth), totalPeriods) - 1);
    System.out.println("each loans : " + eachLoans);
    return eachLoans;
  }

  public double remainLoans() {
    double remainLoans = totalLoans * Math.pow((1 + rateOfMonth), periods) -
                         payLoansOfEachPeriod() * (Math.pow((1 + rateOfMonth), periods) - 1) /
                         rateOfMonth;
    System.out.println("remain loans : " + remainLoans);
    return remainLoans;
  }

  public double firstCost() {
    double downPayment = 407919;
    double tax = 18686.96;
    double maintenanceFunds = 11901;
    double other = 360;

    double firstCost = downPayment + tax + maintenanceFunds + other;
    System.out.println("first cost : " + firstCost);

    return firstCost;
  }

  public double lossOfPayLoans() {
    double eachLoans = payLoansOfEachPeriod();
    double profitRate = 0.1 / 12;
    double loss = 0;
    for (int i = 1; i < periods + 1; i++) {
      loss = loss + eachLoans * Math.pow((1 + profitRate), i);
    }
    System.out.println("loss and total pay loans : " + loss);
    return loss;
  }

  public static void main(String[] args) {
    House house = new House();
    System.out.println(house.years + ", total cost : " +
                       (house.firstCost() + house.lossOfPayLoans() + house.remainLoans()) / 108);
  }

}
