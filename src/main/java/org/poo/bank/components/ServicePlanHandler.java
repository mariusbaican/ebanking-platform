package org.poo.bank.components;

import lombok.Getter;
import org.poo.bank.Tuple;

import java.util.HashMap;
import java.util.Map;

public class ServicePlanHandler {
    private static final Map<Tuple<ServicePlan, ServicePlan>, Double> feeMap = new HashMap<>();
    @Getter
    private static final String currency = "RON";

    static {
        feeMap.put(new Tuple<>(ServicePlan.STANDARD, ServicePlan.SILVER), 100.0);
        feeMap.put(new Tuple<>(ServicePlan.STANDARD, ServicePlan.GOLD), 350.0);
        feeMap.put(new Tuple<>(ServicePlan.STUDENT, ServicePlan.SILVER), 100.0);
        feeMap.put(new Tuple<>(ServicePlan.STUDENT, ServicePlan.GOLD), 350.0);
        feeMap.put(new Tuple<>(ServicePlan.SILVER, ServicePlan.GOLD), 250.0);
    }

    public enum ServicePlan {
        STANDARD("standard"),
        STUDENT("student"),
        SILVER("silver"),
        GOLD("gold");

        final String status;

        ServicePlan(String status) {
            this.status = status;
        }

        @Override
        public String toString() {
            return status;
        }
    }

    public static double getUpgradeFee(final ServicePlan currentPlan, final ServicePlan newPlan) {
        return feeMap.getOrDefault(new Tuple<>(currentPlan, newPlan), -1.0);
    }

    public static double getUpgradeFee(final Tuple<ServicePlan, ServicePlan> planTuple) {
        return feeMap.getOrDefault(planTuple, null);
    }

    public static double adjustFeeForPlan(final ServicePlan currentPlan, final double sum) {
        switch (currentPlan) {
            case STANDARD -> {
                return 1.002 * sum;
            }
            case STUDENT -> {
                return sum;
            }
            case SILVER -> {
                return (Double.compare(sum, 500.0) >= 0 ? 1.001 : 1.0) * sum;
            }
            case GOLD -> {
                return (Double.compare(sum, 500.0) >= 0 ? 0.995 : 1.0) * sum;
            }
            default ->
                throw new IllegalStateException("Invalid ServicePlan: " + currentPlan);
        }
    }
}
